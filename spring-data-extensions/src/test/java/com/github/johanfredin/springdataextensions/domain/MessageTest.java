package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MessageTest extends EntityTest<Message> {

    @Override
    public void testDefaultConstructor() {
        Message message = getEmptyInstance();
        assertEquals("Id should be 0", 0L, message.getId());
        assertNull("Message text should be null", message.getMessage());
        assertNull("Subject text should be null", message.getSubject());
        assertNull("Sender should be null", message.getSender());
        assertNull("Receiver should be null", message.getReceiver());
    }

    @Override
    public void testConstructorWithParameters() {
        Message message = getPopulatedInstance();
        assertEquals("Id should be 0", 0L, message.getId());
        assertEquals("Message Text should be " + TestFixture.FAKE_MESSAGE_TO_MEMBER, TestFixture.FAKE_MESSAGE_TO_MEMBER, message.getMessage());
        assertEquals("Subject should be " + MessageSubject.OFFER_MEMBERSHIP, MessageSubject.OFFER_MEMBERSHIP, message.getSubject());
        assertNotNull("Sender should exist", message.getSender());
        assertNotNull("Receiver should exist", message.getReceiver());
    }

    @Override
    public void testRelations() {
        Message message = getPopulatedInstance();
        Member sender = message.getSender();
        Member receiver = message.getReceiver();

        sender.dropAllMessages();
        receiver.dropAllMessages();

        message.setRelations();

        assertTrue("Sender should have a sent message", sender.getSentMessages().size() == 1);
        assertTrue("Receiver should have a received message", receiver.getReceivedMessages().size() == 1);
    }

    @Override
    public void testCopyData() {
        Message populatedMessage = getPopulatedInstance();
        populatedMessage.setId(TestFixture.FAKE_ID);

        Message emptyMessage = getEmptyInstance();
        emptyMessage.copyDataFromEntity(populatedMessage);

        assertEquals("Empty message Id should now be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyMessage.getId());
        assertNotNull("Sender should now exist", emptyMessage.getSender());
        assertNotNull("Receiver should now exist", emptyMessage.getReceiver());
        assertEquals("Empty message Message Text should now be " + TestFixture.FAKE_MESSAGE_TO_MEMBER, TestFixture.FAKE_MESSAGE_TO_MEMBER, emptyMessage.getMessage());
        assertEquals("Empty message Subject should now be " + MessageSubject.OFFER_MEMBERSHIP, MessageSubject.OFFER_MEMBERSHIP, emptyMessage.getSubject());
    }

    @Override
    protected Message getEmptyInstance() {
        return new Message();
    }

    @Override
    protected Message getPopulatedInstance() {
        return TestFixture.getValidMessage(TestFixture.FAKE_MESSAGE_TO_MEMBER, MessageSubject.OFFER_MEMBERSHIP,
                TestFixture.getValidMembers().get(0), TestFixture.getValidMembers().get(1));
    }

}
