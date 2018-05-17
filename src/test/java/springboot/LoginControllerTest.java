package springboot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import util.TestUtil;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        Class c = LoginController.class;
        this.mockMvc = MockMvcBuilders.standaloneSetup(c).build();
    }

    @Test
    public void login() throws Exception {
        testLoginCheck("test", "123", true);
        testLoginCheck("test", "1234", false);
        testLoginCheck("test1", "1234", false);
    }

    @Test
    public void showPage() throws Exception{
        String url = "/loginPage";
        MvcResult result = getResult(url);
        String expectUrl = "login.html";
        assertForwardUrl(result, expectUrl);
    }

    @Test
    public void logout() throws Exception{
        String url = "/logout";
        HashMap<String, Object> sessionattr = new HashMap<>();
        User user = new User("1", "test");
        sessionattr.put("user", user);
        ResultMatcher status = status().is3xxRedirection();
        MvcResult result = getResult(url, sessionattr, status);
        MockHttpServletRequest req = result.getRequest();
        HttpSession session = req.getSession();
        assert session.getAttribute("user") == null;
    }

    private void testLoginCheck(String userName, String password, boolean canLogin) throws Exception{
        String url = "/loginCheck";
        LinkedMultiValueMap<String, String> params = getParams(userName, password);
        ResultMatcher status = status().is3xxRedirection();
        MvcResult result = getResult(url, params, status);
        String expectUrl = canLogin ? "userPage" : "loginPage";
        assertRedirectUrl(result, expectUrl);
        assertUser(userName, canLogin, result);
    }

    private void assertRedirectUrl(MvcResult result, String expectUrl) {
        MockHttpServletResponse res = result.getResponse();
        String url = res.getRedirectedUrl();
        assert expectUrl.equals(url);
    }

    private void assertForwardUrl(MvcResult result, String expectUrl) {
        MockHttpServletResponse res = result.getResponse();
        String url = res.getForwardedUrl();
        assert expectUrl.equals(url);
    }

    private void assertUser(String expectUserName, boolean canLogin, MvcResult result) {
        MockHttpServletRequest req = result.getRequest();
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");
        if (canLogin) {
            expectUserName.equals(user.getName());
        } else {
            assert user == null;
        }
    }

    private LinkedMultiValueMap<String, String> getParams(String userName, String password) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userName", userName);
        params.add("password", password);
        return params;
    }

    private MvcResult getResult(String url) throws Exception {
        return getResult(url, null, null, null);
    }

    private MvcResult getResult(String url,
                MultiValueMap<String, String> params) throws Exception {
        return getResult(url, null, params, null);
    }

    private MvcResult getResult(String url,
                MultiValueMap<String, String> params,
                ResultMatcher status) throws Exception {
        return getResult(url, status, params, null);

    }

    private MvcResult getResult(String url, HashMap sessionattr,
                ResultMatcher status) throws Exception {
        return getResult(url, status, null, sessionattr);
    }

    private MvcResult getResult(String url, ResultMatcher status,
                MultiValueMap<String, String> params,
                HashMap sessionattr) throws Exception {
        MockHttpServletRequestBuilder builder = getBuilder(url, sessionattr, params);
        if (status == null) {
        status = status().isOk();
        }
        MvcResult result = this.mockMvc.perform(builder)
        .andExpect(status)
        // .andDo(print())
        .andReturn();
        return result;
    }

    private MockHttpServletRequestBuilder getBuilder(String url,
                HashMap sessionattr,
                MultiValueMap<String, String> params) {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        MockHttpServletRequestBuilder builder = get(url).accept(type);
        if (sessionattr != null) {
        builder = builder.sessionAttrs(sessionattr);
        }
        if (params != null) {
        builder = builder.params(params);
        }
        return builder;
    }


}
