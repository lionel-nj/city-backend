package org.citybackend.application.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import org.citybackend.repo.InMemoryCityRepo;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SuggestionControllerTest {


  @MockBean
  SimpleSuggestionService mockService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void suggestions_200() throws Exception {
    when(mockService.rankCities(any(InMemoryCityRepo.class), any(String.class), any(Double.class),
        any(Double.class),
        any(Integer.class), any(Integer.class), any(String[].class))).thenReturn(new JsonObject());

    mockMvc.perform(MockMvcRequestBuilders.get("/suggestions/?q=mont&latitude=85&longitude=78"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void suggestions_400() throws Exception {
    when(mockService.rankCities(any(InMemoryCityRepo.class), any(String.class), any(Double.class),
        any(Double.class),
        any(Integer.class), any(Integer.class), any(String[].class))).thenReturn(new JsonObject());

    mockMvc.perform(MockMvcRequestBuilders.get("/badendpoint/?q=mont&latitude=85&longitude=78"))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
  }
}
