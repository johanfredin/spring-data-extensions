package com.github.johanfredin.springdataextensions.domain;


import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MemberTest extends EntityTest<Member> {

    @Override
    public void testDefaultConstructor() {
        Member member = getEmptyInstance();
        assertEquals("Member id should be 0", 0L, member.getId());
        assertNull("member username should be empty", member.getUserName());
        assertNull("Member password should be empty", member.getPassword());
        assertNull("Member should have no artists yet", member.getArtists());
        assertNull("Member should have no role yet", member.getRole());

        // Properties inherited from DescribingEntity
        assertNull("Member should have no name yet", member.getName());
        assertNull("Member email should be empty", member.getEmail());
        assertNull("Member should have no city yet", member.getCity());
        assertNull("Member should have no country yet", member.getCountry());
    }

    @Override
    public void testConstructorWithParameters() {
        Member member = getPopulatedInstance();
        assertEquals("Username should be " + TestFixture.FAKE_USERNAME, TestFixture.FAKE_USERNAME, member.getUserName());
        assertEquals("Member password should be " + TestFixture.FAKE_PASSWORD, TestFixture.FAKE_PASSWORD, member.getPassword());
        assertEquals("Member email should be " + TestFixture.FAKE_EMAIL, TestFixture.FAKE_EMAIL, member.getEmail());
        assertEquals("Member role should be " + TestFixture.FAKE_ROLE, TestFixture.FAKE_ROLE, member.getRole());
        assertNotNull("Member should have artists", member.getArtists());

        // Properties inherited from DescribingEntity
        assertEquals("Member name should be " + TestFixture.FAKE_NAME, TestFixture.FAKE_NAME, member.getName());
        assertEquals("Member email should be " + TestFixture.FAKE_EMAIL, TestFixture.FAKE_EMAIL, member.getEmail());
        assertEquals("Member city should be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, member.getCity());
        assertEquals("Member country should be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, member.getCountry());
    }

    @Override
    public void testRelations() {
        // Nothing to test...
    }

    @Override
    public void testCopyData() {
        Member emptyMember = getEmptyInstance();
        Member populatedMember = getPopulatedInstance();
        populatedMember.setId(TestFixture.FAKE_ID);

        emptyMember.copyDataFromEntity(populatedMember);
        assertEquals("Member id should now be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyMember.getId());
        assertEquals("Username should now be " + TestFixture.FAKE_USERNAME, TestFixture.FAKE_USERNAME, emptyMember.getUserName());
        assertEquals("Member password should now be " + TestFixture.FAKE_PASSWORD, TestFixture.FAKE_PASSWORD, emptyMember.getPassword());
        assertEquals("Member email should now be " + TestFixture.FAKE_EMAIL, TestFixture.FAKE_EMAIL, emptyMember.getEmail());
        assertEquals("Member role should now be " + TestFixture.FAKE_ROLE, TestFixture.FAKE_ROLE, emptyMember.getRole());
        assertNotNull("Member should now have artists", emptyMember.getArtists());

        // Properties inherited from DescribingEntity
        assertEquals("Member name should now be " + TestFixture.FAKE_NAME, TestFixture.FAKE_NAME, emptyMember.getName());
        assertEquals("Member email should now be " + TestFixture.FAKE_EMAIL, TestFixture.FAKE_EMAIL, emptyMember.getEmail());
        assertEquals("Member city should now be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, emptyMember.getCity());
        assertEquals("Member country should now be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, emptyMember.getCountry());
    }

    @Test
    public void testMessagingMemberToContactPerson() {
        Member sender = getMessengerWithEmptyMessages(123L, 1, false);
        Member receiver = getMessengerWithEmptyMessages(321L, 2, false);


        sender.sendMessage(new Message(TestFixture.FAKE_MESSEGE_TO_VENUE), sender, receiver);

        // Verify the senders message is correct
        Message sendersMessage = sender.getSentMessages().get(0);
        assertEquals("Sender should have 1 message", 1, sender.getAllMessages().size());
        assertEquals("Senders message text should be " + TestFixture.FAKE_MESSEGE_TO_VENUE, TestFixture.FAKE_MESSEGE_TO_VENUE, sendersMessage.getMessage());
        assertEquals("Senders message sender id should be " + sender.getId(), sender.getId(), sendersMessage.getSender().getId());
        assertEquals("Senders message receiver id should be " + receiver.getId(), receiver.getId(), sendersMessage.getReceiver().getId());

        // Verify the receivers message is correct
        Message receiversMessage = receiver.getReceivedMessages().get(0);
        assertEquals("Receiver should have 1 message", 1, receiver.getAllMessages().size());
        assertEquals("Receivers message text should be " + TestFixture.FAKE_MESSEGE_TO_VENUE, TestFixture.FAKE_MESSEGE_TO_VENUE, receiversMessage.getMessage());
        assertEquals("Receivers message sender id should be " + sender.getId(), sender.getId(), receiversMessage.getSender().getId());
        assertEquals("Receivers message receiver id should be " + receiver.getId(), receiver.getId(), receiversMessage.getReceiver().getId());
    }

    @Test
    public void testMessagingContactPersonToMember() {
        Member sender = getMessengerWithEmptyMessages(123L, 1, false);
        Member receiver = getMessengerWithEmptyMessages(321L, 2, false);

        sender.sendMessage(new Message(TestFixture.FAKE_RESPONSE_FROM_VENUE), sender, receiver);

        // Verify the senders message is correct
        Message sendersMessage = sender.getSentMessages().get(0);
        assertEquals("Sender should have 1 message", 1, sender.getAllMessages().size());
        assertEquals("Senders message text should be " + TestFixture.FAKE_RESPONSE_FROM_VENUE, TestFixture.FAKE_RESPONSE_FROM_VENUE, sendersMessage.getMessage());
        assertEquals("Senders message sender id should be " + sender.getId(), sender.getId(), sendersMessage.getSender().getId());
        assertEquals("Senders message receiver id should be " + receiver.getId(), receiver.getId(), sendersMessage.getReceiver().getId());

        // Verify the receivers message is correct
        Message receiversMessage = receiver.getReceivedMessages().get(0);
        assertEquals("Receiver should have 1 message", 1, receiver.getAllMessages().size());
        assertEquals("Receivers message text should be " + TestFixture.FAKE_RESPONSE_FROM_VENUE, TestFixture.FAKE_RESPONSE_FROM_VENUE, receiversMessage.getMessage());
        assertEquals("Receivers message sender id should be " + sender.getId(), sender.getId(), receiversMessage.getSender().getId());
        assertEquals("Receivers message receiver id should be " + receiver.getId(), receiver.getId(), receiversMessage.getReceiver().getId());
    }

    @Test
    public void testSendMessageWithAttachment() {
        Member sender = getMessengerWithEmptyMessages(123L, 1, false);
        Member receiver = getMessengerWithEmptyMessages(321L, 2, false);

        Message message = new Message(TestFixture.FAKE_MESSAGE_TO_MEMBER, MessageSubject.REQUEST_MEMBERSHIP);
        sender.sendMessage(message, sender, receiver, sender.getArtists().get(0));

        // Verify the senders message is correct
        Message sendersMessage = sender.getSentMessages().get(0);
        assertEquals("Sender should have 1 message", 1, sender.getAllMessages().size());
        assertEquals("Senders message text should be " + TestFixture.FAKE_MESSAGE_TO_MEMBER, TestFixture.FAKE_MESSAGE_TO_MEMBER, sendersMessage.getMessage());
        assertEquals("Senders message sender id should be " + sender.getId(), sender.getId(), sendersMessage.getSender().getId());
        assertEquals("Senders message receiver id should be " + receiver.getId(), receiver.getId(), sendersMessage.getReceiver().getId());

        // Verify the receivers message is correct
        Message receiversMessage = receiver.getReceivedMessages().get(0);
        assertEquals("Receiver should have 1 message", 1, receiver.getAllMessages().size());
        assertEquals("Receivers message text should be " + TestFixture.FAKE_MESSAGE_TO_MEMBER, TestFixture.FAKE_MESSAGE_TO_MEMBER, receiversMessage.getMessage());
        assertEquals("Receivers message sender id should be " + sender.getId(), sender.getId(), receiversMessage.getSender().getId());
        assertEquals("Receivers message receiver id should be " + receiver.getId(), receiver.getId(), receiversMessage.getReceiver().getId());

        String summary = message.getSummary();
        assertEquals("Summary should be ", "Member zolost wishes to become a Member of artist Taikes", summary);
    }

    @Test
    public void testAddMessage() {
        Member messenger = getMessengerWithEmptyMessages();

        assertTrue("Messages list should be empty", messenger.getAllMessages().isEmpty());
        messenger.addMessage(TestFixture.getValidMessage(), MessageType.SENT);
        assertEquals("Messages size should now be 1", 1, messenger.getAllMessages().size());
    }

    @Test
    public void testAddMessageWithAType() {
        Member messenger = getMessengerWithEmptyMessages();

        assertTrue("Messages list should be empty", messenger.getSentMessages().isEmpty());

        Message message = TestFixture.getValidMessage();

        messenger.addMessage(message, MessageType.SENT);
        assertEquals("Messages size should now be 1", 1, messenger.getSentMessages().size());
        assertEquals("Message type in list should now be " + MessageType.SENT, 1, messenger.getSentMessages().size());

        messenger.addMessage(message, MessageType.RECEIVED);
        assertEquals("Messages size should now be 1", 1, messenger.getReceivedMessages().size());
        assertEquals("Message type in list should now be " + MessageType.RECEIVED, 1, messenger.getSentMessages().size());

        assertTrue("Messages in both lists should still be the same", messenger.getSentMessages().get(0).equals(messenger.getReceivedMessages().get(0)));
    }

    @Test
    public void testRemoveMessage() {
        Member messenger = getMessengerWithEmptyMessages(true);
        Message sentMessageToRemove = messenger.getSentMessages().get(0);
        Message receivedMessageToRemove = messenger.getReceivedMessages().get(0);

        messenger.removeSentMessage(sentMessageToRemove);
        assertEquals("Sent Messages should now be " + (TestFixture.LIST_SIZE - 1), TestFixture.LIST_SIZE - 1, messenger.getSentMessages().size());
        messenger.removeReceivedMessage(receivedMessageToRemove);
        assertEquals("Received Messages should now be " + (TestFixture.LIST_SIZE - 1), TestFixture.LIST_SIZE - 1, messenger.getReceivedMessages().size());
    }

    @Test
    public void testDropAllMessages() {
        Member messenger = getMessengerWithEmptyMessages(true);

        assertEquals("Messages should be " + (TestFixture.LIST_SIZE * 2), TestFixture.LIST_SIZE * 2, messenger.getAllMessages().size());
        messenger.dropAllMessages();
        assertTrue("All Messages list should be empty", messenger.getAllMessages().isEmpty());
        assertTrue("Sent Messages list should be empty", messenger.getSentMessages().isEmpty());
        assertTrue("Received Messages list should be empty", messenger.getReceivedMessages().isEmpty());
    }

    @Test
    public void testGetSentMessages() {
        Member messenger = getMessengerWithEmptyMessages(1, 1, true);
        assertEquals("Sent messages should now be 2", 2, messenger.getSentMessages().size());
    }

    @Test
    public void testGetReceivedMessages() {
        Member messenger = getMessengerWithEmptyMessages(1, 1, true);
        assertEquals("Received messages should now be 2", 2, messenger.getReceivedMessages().size());
    }

    @Test
    public void testSendMessage() {
        Member sender = getMessengerWithEmptyMessages(321L, 1, false);
        Member receiver = getMessengerWithEmptyMessages(123L, 2, false);

        sender.sendMessage(new Message(TestFixture.FAKE_MESSAGE_TO_MEMBER), sender, receiver);

        // Verify the receivers message is correct
        Message receiversMessage = receiver.getReceivedMessages().get(0);
        assertEquals("Receiver should have 1 received message", 1, receiver.getReceivedMessages().size());

        // Verify the senders message is correct
        Message sendersMessage = sender.getSentMessages().get(0);
        assertEquals("Sender should have 1 sent message", 1, sender.getSentMessages().size());

        // The 2 messages should still have the same data
        assertTrue("The 2 messages should still be equal", receiversMessage.equals(sendersMessage));
    }

    @Override
    protected Member getEmptyInstance() {
        return new Member();
    }

    @Override
    protected Member getPopulatedInstance() {
        return TestFixture.getValidMember();
    }

    protected Member getPopulatedInstance2() {
        return TestFixture.getValidMember("Klazz", "klasistheman", "Klas");
    }

    /**
     * @param instance the current instance
     * @return The simple class name of the current instance (e.g <b>ContactPerson</b>)
     **/
    protected String getClassName(Member instance) {
        return instance.getClass().getSimpleName();
    }

    public Member getMessengerWithEmptyMessages() {
        return getMessengerWithEmptyMessages(0, 1, false);
    }

    public Member getMessengerWithEmptyMessages(boolean populateMessages) {
        return getMessengerWithEmptyMessages(0, 1, populateMessages);
    }

    public Member getMessengerWithEmptyMessages(int n) {
        return getMessengerWithEmptyMessages(0, n, false);
    }

    public Member getMessengerWithEmptyMessages(long id, int n, boolean populateMessages) {
        Member messenger = null;
        switch (n) {
            case 1:
                messenger = getPopulatedInstance();
                break;
            case 2:
                messenger = getPopulatedInstance2();
                break;
            default:
                messenger = getPopulatedInstance();
        }

        if (id <= 0) {
            messenger.setId(id);
        }

        messenger.setSentMessages(populateMessages ? getPopulatedSentMessages() : getEmptyMessagesList());
        messenger.setReceivedMessages(populateMessages ? getPopulatedReceivedMessages() : getEmptyMessagesList());
        return messenger;
    }

    public List<Message> getPopulatedSentMessages() {
        return TestFixture.getValidMessages(MessageType.SENT);
    }

    public List<Message> getPopulatedReceivedMessages() {
        return TestFixture.getValidMessages(MessageType.RECEIVED);
    }

    public List<Message> getPopulatedReceivedMessages(MessageType type) {
        return TestFixture.getValidMessages(type);
    }

    public List<Message> getEmptyMessagesList() {
        return new ArrayList<Message>();
    }


}

