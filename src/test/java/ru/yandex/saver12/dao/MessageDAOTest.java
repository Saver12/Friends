package ru.yandex.saver12.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import ru.yandex.saver12.dao.impl.MessageDAOImpl;
import ru.yandex.saver12.model.Message;
import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

public class MessageDAOTest extends SetUpTest {

    private static MessageDAO messageDAO = new MessageDAOImpl();

    @Override
    protected IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("message-data.xml"));
    }

    @Test
    public void testGetDialogueList() throws Exception {
        List<User> list = messageDAO.getDialogueList(1);
        assertThat(list.get(0).getName(), is("Megan"));
    }

    @Test
    public void testGetMessageList() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        List<Message> list = messageDAO.getMessageList(request, 1, 2);
        assertThat(list.get(0).getId(), is(1));
    }

    @Test
    public void testCreateMessage() throws Exception {
        Message message = messageDAO.createMessage("Howdy?", 2, 1);
        assertThat(message.getId(), is(greaterThan(1)));
        assertThat(message.getMessage(), is("Howdy?"));
    }

    @Test
    public void testDeleteDialogue() throws Exception {
        messageDAO.deleteDialogue(1, 2);
        changeConnection();
        List<User> list = messageDAO.getDialogueList(1);
        assertThat(list.isEmpty(), is(true));
    }
}