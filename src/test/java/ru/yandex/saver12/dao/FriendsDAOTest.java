package ru.yandex.saver12.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import ru.yandex.saver12.dao.impl.FriendsDAOImpl;
import ru.yandex.saver12.model.User;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FriendsDAOTest extends SetUpTest {

    private static FriendsDAO friendsDAO = new FriendsDAOImpl();

    @Override
    protected IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("friends-data.xml"));
    }

    @Test
    public void testAddFriend() throws Exception {
        friendsDAO.addFriend(1, 2);
        changeConnection();
        List<User> list = friendsDAO.getRequestsList(1);
        assertThat(list.get(0).getName(), is("Megan"));
    }

    @Test
    public void testDeleteFriend() throws Exception {
        friendsDAO.deleteFriend(1, 2);
        changeConnection();
        List<User> list = friendsDAO.getFriendsList(1);
        assertThat(list.isEmpty(), is(true));
    }

    @Test
    public void testDeclineRequest() throws Exception {
        friendsDAO.declineRequest(1, 3);
        changeConnection();
        List<User> list = friendsDAO.getRequestsList(3);
        assertThat(list.isEmpty(), is(true));
    }

    @Test
    public void testAcceptRequest() throws Exception {
        friendsDAO.acceptRequest(1, 3);
        changeConnection();
        List<User> list = friendsDAO.getFriendsList(1);
        assertThat(list.size(), is(2));
    }

    @Test
    public void testGetFriendsList() throws Exception {
        List<User> list = friendsDAO.getFriendsList(1);
        assertThat(list.get(0).getName(), is("Megan"));
    }

    @Test
    public void testGetRequestsList() throws Exception {
        List<User> list = friendsDAO.getRequestsList(3);
        assertThat(list.get(0).getName(), is("Bob"));
    }
}