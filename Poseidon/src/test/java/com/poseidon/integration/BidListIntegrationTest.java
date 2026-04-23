package com.poseidon.integration;


import com.poseidon.domain.BidList;
import com.poseidon.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest                  // Charge tout le contexte Spring (controller + service + repository)
@AutoConfigureMockMvc            // Simule les requêtes HTTP sans démarrer de serveur
@ActiveProfiles("test")          // Utilise application-test.properties (H2)
class BidListIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Le vrai repository — permet de vérifier ce qui est réellement en base
    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    void setUp() {
        // Repart d'une base vide avant chaque test
        bidListRepository.deleteAll();
    }


    // GET — lecture
    @Test
    @WithMockUser
    void getBidList_shouldReturnListView_withBidsFromDatabase() throws Exception {
        // Arrange — on insère directement en base
        bidListRepository.save(new BidList("Account1", "Type1", 10.0));
        bidListRepository.save(new BidList("Account2", "Type2", 20.0));

        // Act + Assert
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));
    }

    @Test
    @WithMockUser
    void showAddForm_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser
    void showUpdateForm_shouldReturnUpdateView_withBidFromDatabase() throws Exception {
        // Arrange — on insère un bid réel en base
        BidList saved = bidListRepository.save(new BidList("Account1", "Type1", 10.0));

        // Act + Assert — on utilise l'id généré par la base
        mockMvc.perform(get("/bidList/update/" + saved.getBidListId()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    // POST — création (validate)

    @Test
    @WithMockUser
    void validate_shouldPersistBidInDatabase_andRedirect_whenValidInput() throws Exception {
        // Act
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("bidQuantity", "10.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        // Assert — vérifie que le bid est bien en base
        assertThat(bidListRepository.findAll()).hasSize(1);
        assertThat(bidListRepository.findAll().get(0).getAccount()).isEqualTo("TestAccount");
    }

    @Test
    @WithMockUser
    void validate_shouldNotPersist_andReturnAddView_whenValidationFails() throws Exception {
        // Act
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "")   // @NotBlank => invalide
                        .param("type", "")       // @NotBlank => invalide
                        .param("bidQuantity", "10.0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasErrors("bidList"));

        // Assert — rien ne doit être persisté
        assertThat(bidListRepository.findAll()).isEmpty();
    }

    // POST — mise à jour (update)

    @Test
    @WithMockUser
    void updateBidList_shouldUpdateInDatabase_andRedirect_whenValidInput() throws Exception {
        //  on insère un bid existant
        BidList saved = bidListRepository.save(new BidList("OldAccount", "OldType", 5.0));

        // Act
        mockMvc.perform(post("/bidList/update/" + saved.getBidListId())
                        .param("account", "NewAccount")
                        .param("type", "NewType")
                        .param("bidQuantity", "99.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        // Avérifie la mise à jour en base
        BidList updated = bidListRepository.findById(saved.getBidListId()).orElseThrow();
        assertThat(updated.getAccount()).isEqualTo("NewAccount");
        assertThat(updated.getBidQuantity()).isEqualTo(99.0);
    }

    @Test
    @WithMockUser
    void updateBidList_shouldNotUpdate_andReturnUpdateForm_whenInvalidInput() throws Exception {
        // Arrange
        BidList saved = bidListRepository.save(new BidList("OldAccount", "OldType", 5.0));

        // Act
        mockMvc.perform(post("/bidList/update/" + saved.getBidListId())
                        .param("account", "")   // @NotBlank => invalide
                        .param("type", "")       // @NotBlank => invalide
                        .param("bidQuantity", "10.0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors());

        // Assert — la base n'a pas changé
        BidList unchanged = bidListRepository.findById(saved.getBidListId()).orElseThrow();
        assertThat(unchanged.getAccount()).isEqualTo("OldAccount");
    }

    // DELETE

    @Test
    @WithMockUser
    void deleteBidList_shouldRemoveFromDatabase_andRedirect() throws Exception {
        // Arrange
        BidList saved = bidListRepository.save(new BidList("Account1", "Type1", 10.0));

        // Act
        mockMvc.perform(get("/bidList/delete/" + saved.getBidListId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        // Assert — le bid ne doit plus être en base
        assertThat(bidListRepository.findById(saved.getBidListId())).isEmpty();
    }
}

