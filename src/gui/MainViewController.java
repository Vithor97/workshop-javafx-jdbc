package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());// aqui estou injetando o new DepartmentService() do método  setDepartmentService  da classe DepartmentListController
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x ->{});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); // uso esse FXMLLoader para ler a absoluteName
			VBox newVbox = loader.load(); // assimm vai carregar o parametro loader e passar para o obj do tipo Vox
			
			Scene mainScene = Main.getMainScene();// Estou pegando referencia da Cena da classe Main no método estatico getMainScene()
			
			VBox mainVBox =(VBox)((ScrollPane) mainScene.getRoot()).getContent();//metodo getroot pega o primeiro elemento da minha MainView que é o scroll Pane
			
			Node mainMenu = mainVBox.getChildren().get(0);//primeiro Children da janela VBox principal
			mainVBox.getChildren().clear(); // limpo o conteudo
			mainVBox.getChildren().add(mainMenu); // add o Main menu na mainVbox
			mainVBox.getChildren().addAll(newVbox.getChildren()); // E acrescento junto com o mainMenu o os Children do newVbox
			
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
