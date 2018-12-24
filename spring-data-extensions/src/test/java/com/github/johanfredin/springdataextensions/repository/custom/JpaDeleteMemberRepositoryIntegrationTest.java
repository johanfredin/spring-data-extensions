package com.github.johanfredin.springdataextensions.repository.custom;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.ArtistRepository;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaDeleteMemberRepositoryIntegrationTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DeleteMemberRepository deleteMemberRepository;

    /**
     * Removing a member should also remove the artist(s) of that member if
     * the artist(s) only belong to the member we are about to remove
     */
    @Test
    public void testDelete() {
        // Get the fully populated first entity containing 2 artists that are persisted as well
        Member johan = getFullyPopulatedPersistedEntity(true);

        // Create another member
        Member alex = TestFixture.getValidMemberWithoutReferences("Alex", "hetgurka123", "Alex Hedling");
        // Create another artist
        Artist aroma = TestFixture.getValidArtist("Aroma", "Mölndal", "Sweden");

        // Persist alex
        this.memberRepository.save(alex);
        // Persist the second artist
        this.artistRepository.save(aroma);

        // Crate a list of artists containing only Aroma and give it to alex
        // put alex in a list and give it to aroma so that the relation betweem alex amd aroma is bi-directional
        // Also add aroma to johan
        aroma.setMembers(TestFixture.getBiDirectionalMembersForArtist(aroma, alex, johan));

        // aroma should now belong to both johan and alex
        // Update the changes in the database
        this.memberRepository.save(johan, alex);

        // Now we are going to remove Johan from db, first we check his artists
        List<Artist> johansArtists = this.deleteMemberRepository.getArtistsBelongingToOnlyOneMember(johan.getId());
        List<Artist> alexArtists = this.deleteMemberRepository.getArtistsBelongingToOnlyOneMember(alex.getId());

        // Johans unique artists should be 2 whereas Alex unique artists should be 0
        assertEquals("Unique artists belonging only to johan should be 2", 2, johansArtists.size());
        assertEquals("Unique artists belonging only to alex should be 0", 0, alexArtists.size());

        // So far so good. now we are going to remove alex from db, doing so should not remove aroma artist since aroma also belongs to johan
        this.deleteMemberRepository.delete(alex);

        // Verify alex is gone from db
        assertNull("There should be no alex in db now", this.memberRepository.getOne(alex.getId()));
        // Verify alex artist aroma is still in db
        assertNotNull("Aroma should still exist in db", this.artistRepository.getOne(aroma.getId()));

        // Now remove johan from db
        this.deleteMemberRepository.delete(johan);
        // Verify johan is gone from db
        assertNull("There should be no johan in db now", this.memberRepository.getOne(johan.getId()));
        // Now both aroma and johans 2 artists should be gone from db (remember, aroma only belongs to johan now since alex was dropped from db)
        for (Artist artist : johansArtists) {
            assertNull("Artist with id=" + artist.getId() + " should be gone from db", this.artistRepository.getOne(artist.getId()));
        }
        // At last verify that aroma is gone from db
        assertNull("Artist aroma with id=" + aroma.getId() + " should be gone from db", this.artistRepository.getOne(aroma.getId()));
    }

    private Member getFullyPopulatedPersistedEntity(boolean biDirectional) {
        List<Member> members = TestFixture.getValidMembersWithoutReferences();
        members.remove(0);
        Member member = members.get(0);
        List<Artist> artists = TestFixture.getValidArtists();

        if (biDirectional) {
            members.get(0).setArtists(artists);
            artists.get(0).setMembers(members);
            artists.get(1).setMembers(members);
        }

        this.memberRepository.save(member);
        this.artistRepository.saveAll(member.getArtists());
        return member;
    }

}
