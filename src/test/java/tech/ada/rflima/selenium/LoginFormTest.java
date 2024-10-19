    package tech.ada.rflima.selenium;

    import io.github.bonigarcia.wdm.WebDriverManager;
    import org.junit.jupiter.api.*;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.chrome.ChromeDriver;

    import java.util.List;

    public class LoginFormTest {

        WebDriver webDriver;

        @BeforeAll
        public static void setUpAll() {
            WebDriverManager.chromedriver().setup();
        }

        @BeforeEach
        public void setUp() {
            webDriver = new ChromeDriver();
        }

        @AfterEach
        public void aposOTeste() {
            webDriver.quit();
        }

        @Test
        void deveAcessarOGoogle() {
            webDriver.get("https://www.google.com.br");

            String tituloDaPagina = webDriver.getTitle();

            Assertions.assertEquals("Google", tituloDaPagina);
        }

        @Test
        void deveLogarAPartirDoFormulario() throws InterruptedException {
            logar("standard_user", "secret_sauce");
        }

        @Test
        void deveAdicionarProdutosAoCarrinho() {
            logar("standard_user", "secret_sauce");
            adicionarProdutosNoCarrinho(3);
        }

        @Test
        void deveChegarNaPaginaDeResumoDoCarrinho() throws InterruptedException {
            Integer numeroDeItens = 3;
            logar("standard_user", "secret_sauce");
            adicionarProdutosNoCarrinho(numeroDeItens);
            chegarNoResumoDeCarrinho(numeroDeItens);
        }

        @Test
        void deveConcluirACompra() throws InterruptedException {
            Integer numeroDeItens = 3;
            logar("standard_user", "secret_sauce");
            adicionarProdutosNoCarrinho(numeroDeItens);
            chegarNoResumoDeCarrinho(numeroDeItens);

            WebElement buttonCheckout = webDriver.findElement(By.id("checkout"));
            buttonCheckout.click();

            WebElement firstName = webDriver.findElement(By.id("first-name"));
            WebElement lastname = webDriver.findElement(By.id("last-name"));
            WebElement zipCode = webDriver.findElement(By.id("postal-code"));
            WebElement buttonContinue = webDriver.findElement(By.id("continue"));

            firstName.sendKeys("Rodolfo");
            lastname.sendKeys("Ferreira");
            zipCode.sendKeys("77777777");
            buttonContinue.click();

            WebElement buttonFinish = webDriver.findElement(By.id("finish"));
            buttonFinish.click();

            String textOrder  = webDriver.findElement(By.className("complete-header")).getText();

            Assertions.assertEquals("Thank you for your order!", textOrder);
        }

        private void chegarNoResumoDeCarrinho(Integer numeroDeItensNoCarrinho) {
            WebElement shoppingCartLink = webDriver.findElement(By.className("shopping_cart_link"));
            shoppingCartLink.click();

            List<WebElement> items = webDriver.findElement(By.className("cart_list")).findElements(By.className("cart_item"));

            Assertions.assertEquals(numeroDeItensNoCarrinho, items.size());
        }

        private void adicionarProdutosNoCarrinho(Integer numeroDeProdutos) {
            WebElement inventoryList = webDriver.findElement(By.className("inventory_list"));
            List<WebElement> listaDeItens =
                    inventoryList.findElements(By.className("inventory_item"));

            listaDeItens
                    .stream()
                    .limit(numeroDeProdutos)
                    .forEach(produto -> {
                        WebElement button = produto.findElement(By.xpath("//*[starts-with(@id, 'add-to-cart')]"));
                        button.click();
                    });

            String numeroDeItens = webDriver.findElement(By.className("shopping_cart_badge")).getText();

            Assertions.assertEquals(numeroDeProdutos, Integer.valueOf(numeroDeItens));
        }


        private void logar(String username, String password) {
            webDriver.get("https://www.saucedemo.com/");
            WebElement usernameElement = webDriver.findElement(By.id("user-name"));
            WebElement passwordElement = webDriver.findElement(By.id("password"));
            WebElement loginButton = webDriver.findElement(By.id("login-button"));

            usernameElement.sendKeys(username);
            passwordElement.sendKeys(password);
            loginButton.click();

            String productSpan = webDriver.findElement(By.className("title")).getText();

            Assertions.assertEquals("Products", productSpan);
        }

    }
