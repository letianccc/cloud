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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceView;
import util.TestUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springboot.Util.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@RunWith(MockitoJUnitRunner.class)
// @RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
 @WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        Class c = HomeController.class;
        this.mockMvc = MockMvcBuilders.standaloneSetup(HomeController.class).build();
        String databaseName = "hadoop";
        TestUtil.initData(databaseName);
    }

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void show() throws Exception{
        User user = new User("1", "test");
        HashMap<String, Object> sessionattr = new HashMap<>();
        sessionattr.put("user", user);

        String url = "/userPage";
        MvcResult result = getResult(url, sessionattr);

        MockHttpServletRequest req = result.getRequest();
        HttpSession session = req.getSession();
        ArrayList<File> files = (ArrayList<File>)session.getAttribute("files");
        for (int i = 0; i < files.size(); i++) {
            File f = files.get(i);
            String expectId = String.valueOf(i+1);
            String prefix = "file";
            String expectFileName = prefix + expectId;
            assert expectId.equals(f.getId());
            assert expectFileName.equals(f.getName());
        }

        MockHttpServletResponse res = result.getResponse();
        String expectUrl = "home.html";
        assert expectUrl.equals(res.getForwardedUrl());
    }

    private MvcResult getResult(String url) throws Exception {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        MvcResult result = this.mockMvc.perform(get(url)
        .accept(type))
        .andExpect(status().isOk())
        // .andDo(print())
        .andReturn();
        return result;
    }

    private MvcResult getResult(String url, HashMap sessionattr) throws Exception {
       MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
       MvcResult result = this.mockMvc.perform(get(url)
       .sessionAttrs(sessionattr)
       .accept(type))
       .andExpect(status().isOk())
       // .andDo(print())
       .andReturn();
       return result;
   }

    //
    // @Test
    // public void flushUserPage() {
    // }
    //
    // @Test
    // public void showLoginPage() {
    // }
    //
    // @Test
    // public void test1() {
    // }
}
