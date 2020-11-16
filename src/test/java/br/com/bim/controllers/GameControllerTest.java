package br.com.bim.controllers;

import br.com.bim.BimApplication;
import br.com.bim.domains.Game;
import br.com.bim.dtos.input.GameCreateInputDto;
import br.com.bim.dtos.output.GameCreateOutputDto;
import br.com.bim.repositories.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {BimApplication.class})
@ActiveProfiles("test")
public class GameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GameRepository gameRepository;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void create() throws Exception {

        GameCreateInputDto body = new GameCreateInputDto();
        body.setStartValue(new BigDecimal(25000));
        body.setPass("123456");

        ResultActions resultActions = mockMvc.perform(
                post("/game/create")
                        .content(mapper.writeValueAsString(body))
                        .header("Content-Type", "application/json")
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").isNotEmpty());

        GameCreateOutputDto output = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), GameCreateOutputDto.class);
        Game game = gameRepository.findById(output.getCode()).orElse(null);

        assertNotNull(game);
        assertEquals(output.getCode(), game.getCode());
    }

    @Test
    public void createBodyEmpty() throws Exception {
        mockMvc.perform(
                post("/game/create")
                        .header("Content-Type", "application/json")
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void addPlayer() {
    }

    @Test
    public void getAllPlayers() {
    }

    @Test
    public void operation() {
    }

    @Test
    public void transference() {
    }
}