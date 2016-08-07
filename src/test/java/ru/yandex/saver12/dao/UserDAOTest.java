package ru.yandex.saver12.dao;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import ru.yandex.saver12.dao.impl.UserDAOImpl;
import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

public class UserDAOTest extends SetUpTest {

    private static UserDAO userDAO = new UserDAOImpl();

    @Override
    protected IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("dataset.xml"));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = userDAO.getUser("sm@mail.ru", "qwerty");
        assertThat(user.getName(), is("Bob"));
    }

    @Test
    public void testGetUsersByName() throws Exception {
        List<User> users = userDAO.getUsersByName("Bob");
        assertThat(users.get(0).getName(), is("Bob"));
        assertThat(users.size(), is(1));
    }

    @Test
    public void testGetUserById() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = userDAO.getUserById(request, 2, 100);
        assertThat(user.getId(), is(2));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User expected = new User("Pikachu", "Pokeson", "bunny@mail.ru", "USA", new Date(), "none", "none", "in love", 2, 11);
        userDAO.updateUser(expected);
        changeConnection();
        User actual = userDAO.getUser("bunny@mail.ru", "qwerty");
        assertThat(actual.getName(), is("Pikachu"));
        assertThat(actual.getId(), is(2));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = userDAO.createUser("Paul", "Emerson", "sh@info.com", "Hungary", "www");
        assertThat(user.getPhotoId(), is(0));
        assertThat(user.getId(), is(greaterThan(2)));
    }
}