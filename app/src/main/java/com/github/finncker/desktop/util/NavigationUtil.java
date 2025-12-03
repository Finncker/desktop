package com.github.finncker.desktop.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NavigationUtil {

    public static void navigateTo(BorderPane rootPane, String fxmlPath) {
        try {
            // Limpar o conteúdo anterior do center antes de carregar o novo
            rootPane.setCenter(null);

            Parent view = FXMLLoader.load(NavigationUtil.class.getResource(fxmlPath));
            if (view != null) {
                // Se o view carregado for um BorderPane, extrair apenas o conteúdo do center
                // para evitar duplicação do side panel
                if (view instanceof BorderPane) {
                    BorderPane loadedPane = (BorderPane) view;
                    Parent centerContent = (Parent) loadedPane.getCenter();
                    if (centerContent != null) {
                        // Remover o centerContent do BorderPane carregado para evitar referências
                        // duplicadas
                        loadedPane.setCenter(null);
                        // Adicionar apenas o conteúdo do center ao rootPane
                        rootPane.setCenter(centerContent);
                    } else {
                        log.warn("BorderPane carregado de {} não possui conteúdo no center", fxmlPath);
                        // Se não houver center, usar o view completo (não ideal, mas evita erro)
                        rootPane.setCenter(view);
                    }
                } else {
                    // Se não for BorderPane, usar diretamente
                    rootPane.setCenter(view);
                }
            } else {
                log.error("FXML {} retornou null", fxmlPath);
            }
        } catch (IOException e) {
            log.error("Erro ao navegar para {}: {}", fxmlPath, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erro inesperado ao navegar para {}: {}", fxmlPath, e.getMessage(), e);
        }
    }

    public static Parent loadFXML(String fxmlPath) {
        try {
            Parent parent = FXMLLoader.load(NavigationUtil.class.getResource(fxmlPath));
            if (parent == null) {
                log.error("FXML {} retornou null após carregamento", fxmlPath);
            }
            return parent;
        } catch (IOException e) {
            log.error("Erro ao carregar FXML {}: {}", fxmlPath, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Erro inesperado ao carregar FXML {}: {}", fxmlPath, e.getMessage(), e);
            return null;
        }
    }
}
