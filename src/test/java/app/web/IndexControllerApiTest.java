package app.web;

import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getIndexEndpointAndReturnIndexView() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getLoginEndpointWithErrorParameterAndReturnLoginView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login").param("error", "");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void getLoginEndpointAndReturnLoginView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    void getPetCareEndpointAndReturnPetCareView() throws Exception {

        MockHttpServletRequestBuilder request = get("/pets-care");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("pets-care"));
    }

    @Test
    void getCatCareEndpointAndReturnCatCareView() throws Exception {

        MockHttpServletRequestBuilder request = get("/cat-care");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("cat-care"));
    }

    @Test
    void getDogCareEndpointAndReturnDogCareView() throws Exception {

        MockHttpServletRequestBuilder request = get("/dog-care");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("dog-care"));
    }

    @Test
    void getAboutUsEndpointAndReturnAboutUsView() throws Exception {

        MockHttpServletRequestBuilder request = get("/about-us");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("about-us"));
    }
}
