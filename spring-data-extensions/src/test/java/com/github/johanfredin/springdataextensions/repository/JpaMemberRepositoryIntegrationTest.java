package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.github.johanfredin.springdataextensions.util.RepositoryUtil.lP;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaMemberRepositoryIntegrationTest extends EmailHolderRepositoryTest<Member, MemberRepository> {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private MessageRepository messageRepo;

    @Override
    protected MemberRepository getRepository() {
        return this.memberRepository;
    }

    @Override
    protected Member getEntity1() {
        return TestFixture.getValidMembers().get(0);
    }

    @Override
    protected Member getEntity2() {
        return TestFixture.getValidMembers().get(1);
    }

    /**
     * Custom implementation.
     *
     * @return a persisted member with 2
     * persisted artists who themselves are connected to the persisted member
     */
    @Override
    protected Member getPersistedEntity1() {
        Member member = getEntity1();
        member.setArtists(TestFixture.getValidArtists());
        List<Member> members = new ArrayList<Member>();
        members.add(member);
        for (Artist artist : member.getArtists()) {
            this.artistRepo.save(artist);
            artist.setMembers(members);
        }
        return getRepository().save(member);
    }

    @Override
    protected Member getPersistedEntity2() {
        Member member = getEntity2();
        member.setArtists(TestFixture.getValidArtists("Nirvana", "Yolo"));
        List<Member> members = new ArrayList<Member>();
        members.add(member);
        for (Artist artist : member.getArtists()) {
            this.artistRepo.save(artist);
            artist.setMembers(members);
        }
        return getRepository().save(member);
    }

    @Override
    public void testCascadePersist() {
        // Member has no cascade persisting with artists,
        // but at least we can verify that the artist does not get persisted
        Member member = getEntity1();
        member.setArtists(TestFixture.getValidArtists());

        // Verify that the member has artists
        assertNotNull("Member should have unpersisted artists", member.getArtists());

        getRepository().save(member);

        // Verify that the member still has artists after persist
        assertNotNull("Member should still have unpersisted artists", member.getArtists());

        // Verify that the artists are not persisted
        for (Artist artist : member.getArtists()) {
            assertTrue("Artist id should be < 1", artist.getId() < 1);
        }
    }

    @Override
    public void testCascadeMerge() {
        // Acquire persisted member
        Member member = this.getPersistedEntity1();

        List<Artist> artists = member.getArtists();

        // Verify the artists are persisted, they should be when calling this #getPersistedEntity1()
        for (Artist artist : artists) {
            assertNotNull("Artist should be persisted", this.artistRepo.getOne(artist.getId()));
        }

        // Verify the messages are persisted
        for (Message message : member.getReceivedMessages()) {
            assertNotNull("Message should be persisted", this.messageRepo.getOne(message.getId()));
        }
        for (Message message : member.getSentMessages()) {
            assertNotNull("Message should be persisted", this.messageRepo.getOne(message.getId()));
        }

        // Change some properties
        member.setUserName("Jolo");
        artists.get(0).setName("Beach Boys");
        artists.get(1).setName("The Beatles");

        member.getSentMessages().get(0).setMessage("I hate Nickelback!");

        // Get a second member and give him access to one artist of ours
        Member member2 = getPersistedEntity2();
        member.grantArtistAccess(artists.get(0), member2);

        // Update the members
        getRepository().save(member);

        // Acquire updated child entities from db
        Artist dbArtist1 = this.artistRepo.getOne(artists.get(0).getId());
        Artist dbArtist2 = this.artistRepo.getOne(artists.get(1).getId());

        Message dbMessage1 = this.messageRepo.getOne(member.getSentMessages().get(0).getId());


        // Verify the changes
        assertEquals("Persisted artist nr 1 should now have name=\"Beach Boys\"", "Beach Boys", dbArtist1.getName());
        assertTrue("Persisted artist nr 1 should now also belong to member2", dbArtist1.getMembers().contains(member2));
        assertEquals("Persisted artist nr 2 should now have name=\"The Beatles\"", "The Beatles", dbArtist2.getName());
        assertEquals("Persisted message nr 1 should now have message text=\"I hate Nickelback!\"", "I hate Nickelback!", dbMessage1.getMessage());

        // Verify child entities member update was successful
        assertEquals("Persisted artist nr 1 should now Member with username=\"Jolo\"", "Jolo", dbArtist1.getMembers().get(0).getUserName());
        assertEquals("Persisted artist nr 2 should now Member with username=\"Jolo\"", "Jolo", dbArtist2.getMembers().get(0).getUserName());
    }

    @Override
    public void testCascadeDelete() {
//		Member member1 = TestFixture.getValidMemberWithoutReferences("Jolo", "lizardKING", "Johan");
//		Member member2 = TestFixture.getValidMemberWithoutReferences("Hoho", "@$$h0Lie", "Jonas");
//		
//		List<Member> members = new ArrayList<Member>();
//		members.add(member1);
//		members.add(member2);
//		
//		// Create new artist and assign it the members
//		Artist artist = new Artist("Taikes", "Gothenburg", "Sweden", null, null, null, null, null, null, "info@taikes.com", Genre.GRUNGE_ROCK);
//		artist.setMembers(members);
//		
//		// Add artist to a map and have the members set it to this
//		List<Artist> artists = new ArrayList<Artist>();
//		artists.add(artist);
//		
//		member1.setArtists(artists);
//		member2.setArtists(artists);
//		
//		// Persist the artist
//		this.artistRepo.save(artist);
//		
//		// Persist the members
//		super.save(member1, member2);
//		
//		// The artist now have 2 members and the members have the artist
//		// Lets remove the first member
//		getRepository().delete(member1);
//		
//		// Verify that the artist still existsById
//		assertNotNull("Artist should still exist since it still has member with id=" + member2.getId(), artistRepo.findOne(artist.getId()));
//		
//		// Now remove the second member
//		getRepository().delete(member2);
//		
//		// Now the artist should have been removed as well, verify that
//		assertNull("Artist should no longer exist in db since both members it was assigned to are deleted", artistRepo.findOne(artist.getId()));
    }

    @Test
    public void testGetEntityWithChildren() {
        long id = getFullyPopulatedPersistedEntity(true).getId();
        Member member = getRepository().getMemberWithChildren(id, true, true);

        assertNotNull("Member should have artists", member.getArtists());
        for (Artist artist : member.getArtists()) {
            assertTrue("artist should have at least one member that matches our member", artist.getMembers().contains(member));
        }

        assertNotNull("Member should have sent mMessage sendMessage(Message message, Member sender, Member receiver, boolean updateMembers, boolean persistMessage);essages", member.getSentMessages());
        assertNotNull("Member should have received messages", member.getReceivedMessages());
    }

    @Test
    public void testGetMembersForArtist() {
        List<Member> members = new ArrayList<Member>();
        List<Artist> artists = new ArrayList<Artist>();
        Member member1 = getRepository().save(getEntity1());
        Member member2 = getRepository().save(getEntity2());

        Artist artist = TestFixture.getValidArtists().get(0);
        this.artistRepo.save(artist);

        artists.add(artist);
        members.add(member1);
        members.add(member2);

        artist.setMembers(members);
        members.get(0).setArtists(artists);
        members.get(1).setArtists(artists);

        this.artistRepo.save(artist);

        List<Member> membersForArtist = getRepository().findAllByArtistsIn(artist);
        assertEquals("There should be 2 members where the artist was our artist", 2, membersForArtist.size());
    }

    @Test
    public void testMessageAnotherMember() {
        Member sender = getEntity1();
        Member receiver = getEntity2();

        sender.dropAllMessages();
        receiver.dropAllMessages();

        super.save(sender, receiver);

        Message message = new Message(TestFixture.FAKE_MESSAGE_TO_MEMBER, MessageSubject.OFFER_MEMBERSHIP);
        this.messageRepo.save(message);

        sender.sendMessage(message, sender, receiver);

        assertTrue("Calling sendMessage with all flags set to true should result in the message now being persisted", message.isExistingEntity());

        assertTrue("Messages sender should be the same as our sender", message.getSender().equals(sender));
        assertTrue("Messages receiver should be the same as our receiver", message.getReceiver().equals(receiver));

        assertTrue("Sender should have the message as type SENT", sender.getSentMessages().contains(message));
        assertTrue("Receiver should have the message as type RECEIVED", receiver.getReceivedMessages().contains(message));
    }

    @Test
    public void testGrantArtistAccess() {
        // Get a member and drop artists before persisting to make sure the member dont already have that artist
        Member requester = getEntity1();
        requester.dropArtists();
        getRepository().save(requester);


        Member owner = getFullyPopulatedPersistedEntity(true);

        Artist artistToGrant = owner.getArtists().get(0);

        // Make sure requesting member don't have the artist already
        assertFalse("Requesting member should not have the artist we are going to give permission to", requester.getArtists().contains(artistToGrant));

        // Grant permission
        owner.grantArtistAccess(artistToGrant, requester);

        // Verify
        assertTrue("Requesting member should now have the artist", requester.getArtists().contains(artistToGrant));
        assertTrue("Artist should now also belong to requesting member", artistToGrant.getMembers().contains(requester));
    }

    @Test
    public void testFindMembersMatchingUserNameOrEmail() {
        // Fetch unpersisted entities 1 and 2
        Member johan = getEntity1();
        Member alex = getEntity2();

        String email = "info@taikes";

        // Give them different usernames but very similar emails
        johan.setUserName("Johan");
        johan.setEmail("info@taikes1.com");
        alex.setUserName("Alex");
        alex.setEmail("info@taikes2.com");

        // Persist the entities
        super.save(johan, alex);

        // Do a query using username, the resulting list should contain 1 record
        List<Member> userNameResults = getRepository().findAllByUserNameContaining("Joha");
        // Do a query on emails, the resulting list should contain 2 records
        List<Member> emailResults = getRepository().findAllByEmailIsLike(lP(email));
        // Do a email query with limit results, list should contain 1 record
        List<Member> emailLimitedResults = getRepository().findAllByEmailIsLike(lP(email), new PageRequest(0, 1)).getContent();

        // Verify size
        assertEquals("UserName results should contain one record", 1, userNameResults.size());
        assertEquals("Email results should contain 2 records", 2, emailResults.size());
        assertEquals("Email limited results should contain 1 record", 1, emailLimitedResults.size());

        // Verify data in results is correct
        assertTrue("UserName result should contain one record that matches Johan member", userNameResults.get(0).equals(johan));
        assertTrue("Email results should contain both Johan and Alex", emailResults.contains(johan) && emailResults.contains(alex));
        assertTrue("Limite email results should contain either johan or alex", emailLimitedResults.contains(johan) || emailLimitedResults.contains(alex));
    }

    @Test
    public void testIsNoOtherEntityWithUserName() {
        // Get 2 entities and give them the same username
        Member m1 = getEntity1();
        Member m2 = getEntity2();
        String userName = "zolost";
        m2.setUserName(userName);
        m1.setUserName(userName);

        // Save the first user
        getRepository().save(m1);
        // At this point no other user should exist with that username
        assertTrue("there should be no other member with username=" + userName, getRepository().isNoOtherEntityWithUserName(m1.getId(), m1.getUserName()));

        // When checking m2's username there should already be another member with that username
        assertFalse("there should now be another member with username=" + userName, getRepository().isNoOtherEntityWithUserName(m2.getId(), m2.getUserName()));
    }

    @Test
    public void testIsUserNameUnique() {
        // Fetch an entity and give it a new username
        Member zolost = getEntity1();
        String userName1 = "zolost";
        String userName2 = "soho";

        // Save the member
        zolost.setUserName(userName1);
        getRepository().save(zolost);


        // The username should at this point be unique
        assertTrue("username=" + userName2 + " should be unique", getRepository().isUserNameUnique(userName2));
        // The username should no longer be unique
        assertFalse("there should now be another member with username=" + userName1, getRepository().isUserNameUnique(userName1));
    }

    @Override
    protected Member getFullyPopulatedUnpersistedEntity(boolean biDirectional) {
        List<Member> members = TestFixture.getValidMembersWithoutReferences();
        members.remove(0);
        Member member = members.get(0);
        List<Artist> artists = TestFixture.getValidArtists();
        List<Message> sentMessages = TestFixture.getValidSentMessages();
        List<Message> receivedMessages = TestFixture.getValidReceivedMessages();

        if (biDirectional) {
            members.get(0).setArtists(artists);
            members.get(0).setSentMessages(sentMessages);
            members.get(0).setReceivedMessages(receivedMessages);
            artists.get(0).setMembers(members);
            artists.get(1).setMembers(members);
        }

        return member;
    }

    @Override
    protected Member getFullyPopulatedPersistedEntity(boolean biDirectional) {
        Member member = getFullyPopulatedUnpersistedEntity(biDirectional);
        getRepository().save(member);
        this.artistRepo.saveAll(member.getArtists());
        this.messageRepo.saveAll(member.getSentMessages());
        this.messageRepo.saveAll(member.getReceivedMessages());
        return member;
    }


}
