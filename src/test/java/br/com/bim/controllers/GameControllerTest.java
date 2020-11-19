package br.com.bim.controllers;

import br.com.bim.BimApplication;
import br.com.bim.domains.Game;
import br.com.bim.domains.Player;
import br.com.bim.dtos.input.*;
import br.com.bim.dtos.output.AddPlayerGameOutputDto;
import br.com.bim.dtos.output.GameCreateOutputDto;
import br.com.bim.dtos.output.GamePlayersOutputDto;
import br.com.bim.enums.OperationType;
import br.com.bim.repositories.GameRepository;
import br.com.bim.repositories.PlayerRepository;
import br.com.bim.services.GameService;
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

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameService gameService;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String gamePass = "123456";
    private final BigDecimal gameStartValue = new BigDecimal(25000);

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void create() throws Exception {

        GameCreateInputDto body = new GameCreateInputDto();
        body.setStartValue(gameStartValue);
        body.setPass(gamePass);

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
    public void addPlayer() throws Exception {
        AddPlayerGameInputDto body = new AddPlayerGameInputDto();
        String gameCode = createGame();

        body.setGameCode(gameCode);
        body.setPlayerName("Jean");

        mockMvc.perform(
                post("/game/addPlayer")
                        .content(mapper.writeValueAsString(body))
                        .header("Content-Type", "application/json")
        )
                .andDo(print())
                .andExpect(jsonPath("$.value").value(gameStartValue.doubleValue()))
                .andExpect(jsonPath("$.name").value(body.getPlayerName()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllPlayers() throws Exception {
        String gameCode = createGame();
        gameService.addPlayer(new AddPlayerGameInputDto("Jean", gameCode));
        gameService.addPlayer(new AddPlayerGameInputDto("Jamila", gameCode));

        ResultActions resultActions = mockMvc.perform(
                post("/game/players")
                        .content(mapper.writeValueAsString(new GamePlayersInputDto(gameCode, gamePass)))
                        .header("Content-Type", "application/json")
        ).andDo(print())
                .andExpect(status().is2xxSuccessful());

        GamePlayersOutputDto outputDto = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), GamePlayersOutputDto.class);

        assertEquals(2, outputDto.getPlayers().size());
    }

    @Test
    public void operation() throws Exception {
        String gameCode = createGame();
        AddPlayerGameOutputDto playerDto = gameService.addPlayer(new AddPlayerGameInputDto("Jean", gameCode));

        OperationInputDto body = new OperationInputDto();
        body.setCode(gameCode);
        body.setPass(gamePass);
        body.setPlayerId(playerDto.getPlayerId());
        body.setOperationType(OperationType.SUM.getType());
        body.setValue(new BigDecimal(1000));

        mockMvc.perform(
                post("/game/operation")
                        .content(mapper.writeValueAsString(body))
                        .header("Content-Type", "application/json")
        ).andDo(print())
                .andExpect(status().is2xxSuccessful());

        Player player = playerRepository.findById(playerDto.getPlayerId()).orElse(null);

        assertNotNull(player);
        assertEquals(new BigDecimal("26000.00"), player.getTotal());
    }

    @Test
    public void transference() throws Exception {
        String gameCode = createGame();
        AddPlayerGameOutputDto playerDto1 = gameService.addPlayer(new AddPlayerGameInputDto("player1", gameCode));
        AddPlayerGameOutputDto playerDto2 = gameService.addPlayer(new AddPlayerGameInputDto("player2", gameCode));

        TransferenceInputDto body = new TransferenceInputDto();
        body.setPayer(playerDto1.getPlayerId());
        body.setReceiver(playerDto2.getPlayerId());
        body.setCode(gameCode);
        body.setValue(new BigDecimal(10000));

        mockMvc.perform(
                post("/game/transference")
                        .content(mapper.writeValueAsString(body))
                        .header("Content-Type", "application/json")
        ).andDo(print())
                .andExpect(status().is2xxSuccessful());

        Player player1 = playerRepository.findById(playerDto1.getPlayerId()).orElse(null);
        Player player2 = playerRepository.findById(playerDto2.getPlayerId()).orElse(null);
        assertNotNull(player1);
        assertNotNull(player2);

        assertEquals(new BigDecimal("15000.00"), player1.getTotal());
        assertEquals(new BigDecimal("35000.00"), player2.getTotal());
    }

    private String createGame() {
        GameCreateInputDto inputDto = new GameCreateInputDto();
        inputDto.setPass(gamePass);
        inputDto.setStartValue(new BigDecimal(25000));
        return gameService.createGame(inputDto).getCode();
    }
}