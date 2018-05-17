package springboot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import util.TestUtil;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        Class c = RegisterController.class;
        this.mockMvc = MockMvcBuilders.standaloneSetup(c).build();
    }



    // @Test
    // public void showPage() throws Exception {
    //     String url = "/registerPage";
    //     MvcResult result = getResult(url);
    //     String expectUrl = "register.html";
    //     assertForwardUrl(result, expectUrl);
    // }

    @Test
    public void register() {
        String url = "/loginCheck";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userName", "tset1");
        params.add("password1", "1234");
        params.add("password2", "1234");
        ResultMatcher status = status().is3xxRedirection();
        MvcResult result = getResult(url, params, status);
    }


    private void assertForwardUrl(MvcResult result, String expectUrl) {
        MockHttpServletResponse res = result.getResponse();
        String url = res.getForwardedUrl();
        assert expectUrl.equals(url);
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


}
