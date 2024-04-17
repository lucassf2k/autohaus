package client;

import authentication.ImplAuthenticationRemote;
import authentication.User;
import authentication.UserTypes;
import crypto.HMAC;
import crypto.RSA;
import firewall.Package;
import firewall.ProxyReverse;
import firewall.ProxyReverseProtocol;
import gateway.GatewayRemote;
import gateway.ImplGatewayRemote;
import sdc.ImplSdcService;
import sdc.SdcService;
import shared.CarCategories;
import shared.Message;
import shared.MessageTypes;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ImplClient {
    private final ProxyReverseProtocol proxyReverseStub;
    private static final Scanner scan = new Scanner(System.in);
    private final int PORT;
    private final SdcService sdcStub;
    private final BigInteger RSA_PRIVATE_KEY;
    private final BigInteger RSA_PUBLIC_KEY;
    private final BigInteger RSA_MODULUS;

    public ImplClient(final int port) throws RemoteException, NotBoundException {
        final var registryProxy = LocateRegistry.getRegistry(ProxyReverse.PORT);
        this.proxyReverseStub = (ProxyReverseProtocol) registryProxy.lookup("proxy");
        final var registrySdc = LocateRegistry.getRegistry(ImplSdcService.PORT);
        this.sdcStub = (SdcService) registrySdc.lookup("sdc");
        final var rsaKeys = sdcStub.getRSAKeys();
        RSA_PRIVATE_KEY = rsaKeys.privateKey();
        RSA_PUBLIC_KEY = rsaKeys.publicKey();
        RSA_MODULUS = rsaKeys.modulus();
        this.PORT = port;
        exec();
    }

    private void exec() {
        try {
            var isUserValid = this.login();
            this.menu(isUserValid);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private ImplAuthenticationRemote.Credentials login() throws RemoteException {
        boolean isLogged = Boolean.FALSE;
        ImplAuthenticationRemote.Credentials credentials = null;
        while (!isLogged) {
            System.out.println(" === Bem vindo === ");
            System.out.print("Informe seu email: ");
            final var email = scan.nextLine();
            System.out.print("Informe sua senha: ");
            final var password = scan.nextLine();
            final var content = email + "-" + password;
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(content, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.LOGIN,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            if (Objects.isNull(response)) {
                System.out.println("rejeitando pelo proxy...");
                continue;
            };
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                continue;
            }
            final var splitRawContent = rawContent.split("-");
            final var isRegistered = splitRawContent[0];
            final var userType = splitRawContent[1];
            if (Integer.parseInt(isRegistered) == 1) {
                isLogged = Boolean.TRUE;
                UserTypes userTypeCredential = UserTypes.CUSTOMER;
                if (userType.equals("EMPLOYEE")) userTypeCredential = UserTypes.EMPLOYEE;
                credentials = new ImplAuthenticationRemote.Credentials(isLogged, userTypeCredential);
            } else {
                System.out.println("login incorreto!");
            }
        }
        return credentials;
    }

    private void menu(ImplAuthenticationRemote.Credentials user) {
        int logged = 10;
        System.out.println("== O que quer fazer? ==");
        if (user.useType().equals(UserTypes.CUSTOMER)) {
            // listar, pesquisar, exibir quantidade de carros e comprar
            while (logged != 0) {
                System.out.println("""
                        1 - Listar carros
                        2 - Pesquisar carro
                        3 - Quantidade de carros
                        4 - Comprar carro
                        
                        Digite "0" para fechar o sistema
                        """);
                System.out.print("Opção: ");
                logged = scan.nextInt();
                scan.nextLine();
                switch (logged) {
                    case 0: {
                        logged = 0;
                        break;
                    }
                    case 1: {
                        listCar();
                        break;
                    }
                    case 2: {
                        searchCar();
                        break;
                    }
                    case 3: {
                        numberOfCars();
                        break;
                    }
                    case 4: {
                        buyCar();
                        break;
                    }
                    default: {
                        System.out.println("Nenhuma opção correspondente!");
                        break;
                    }
                }
            }
        } else {
            //  listar, pesquisar, exibir quantidade de carros, comprar, apagar e atualizar
            while (logged != 0) {
                System.out.println("""
                        1 - Listar carros
                        2 - Pesquisar carro
                        3 - Quantidade de carros
                        4 - Comprar carro
                        5 - Adicionar carro
                        6 - Apagar carro
                        7 - Atualizar carro
                        
                        Digite "sair" para fechar o sistema
                        """);
                System.out.print("Opção: ");
                logged = scan.nextInt();
                scan.nextLine();
                switch (logged) {
                    case 0: {
                        logged = 0;
                        break;
                    }
                    case 1: {
                        listCar();
                        break;
                    }
                    case 2: {
                        searchCar();
                        break;
                    }
                    case 3: {
                        numberOfCars();
                        break;
                    }
                    case 4: {
                        buyCar();
                        break;
                    }
                    case 5: {
                        registerCar();
                        break;
                    }
                    case 6: {
                        removeCar();
                        break;
                    }
                    case 7: {
                        updateCar();
                        break;
                    }
                    default: {
                        System.out.println("Nenhuma opção correspondente!");
                        break;
                    }
                }
            }
        }
    }

    private void listCar() {
        try {
            final var message = new Message(
                    MessageTypes.LIST_CAR,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            if (response.content().isEmpty()) System.out.println("Nenhum carro disponível");
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            else System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void searchCar() {
        System.out.println("""
                \t == escolha o tipo da pesquisa ==
                \t 1 - Por Renavam
                \t 2 - Por Nome
                \t 3 - Por Categoria
                """);
        System.out.print("Opção: ");
        final var option = scan.nextInt();
        scan.nextLine();
        String input = "";
        switch (option) {
            case 1: {
                System.out.println("\t Renavam: ");
                input = scan.nextLine();
                break;
            }
            case 2: {
                System.out.print("\t Nome: ");
                input = scan.nextLine();
                break;
            }
            case 3: {
                System.out.println("""
                        \t 0 - Econômica
                        \t 1 - Intermediário
                        \t 2 - Executivo
                        \t Categoria:
                        """);
                System.out.print("Opção: ");
                input = String.valueOf(scan.nextInt());
                scan.nextLine();
                break;
            }
            default: {
                System.out.println("Nenhuma opção correspondente!");
                break;
            }

        }
        try {
            final var content = option + "-" + input;
            System.out.println(content);
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(content, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.SEARCH_CAR,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        System.out.println();
    }

    private void numberOfCars() {
        try {
            final var message = new Message(
                    MessageTypes.NUMBER_OF_CARS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println("Total de carros: " + rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void buyCar() {
        System.out.print("Renavam: ");
        try {
            final var renavam = scan.nextLine();
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(renavam, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.BUY_CAR,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void registerCar() {
        System.out.println("== Digite os dados que se pede para cadastrar um novo carro ==");
        System.out.print("Renavam: ");
        final var renavam = scan.nextLine();
        System.out.print("Nome: ");
        final var name = scan.nextLine();
        System.out.print("Categoria [0 = Econômica, 1 = Intermediário, 2 = Executivo]: ");
        final var category = scan.nextInt();
        scan.nextLine();
        System.out.print("Ano de fabricação: ");
        final var yearOfManufacture = scan.nextLine();
        System.out.print("Preço: ");
        final var price = scan.nextDouble();
        scan.nextLine();
        try {
            final var content = renavam + "-" + name + "-" + category + "-" + yearOfManufacture + "-" + price;
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(content, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.REGISTER_CAR,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void removeCar() {
        System.out.print("Renavam: ");
        final var renavam = scan.nextLine();
        try {
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(renavam, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.REMOVE_CAR,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void updateCar() {
        System.out.println("== Digite os dados que se pede para atualizar um carro ==");
        System.out.print("Renavam: ");
        final var renavam = scan.nextLine();
        System.out.print("Nome: ");
        final var name = scan.nextLine();
        System.out.print("Categoria [0 = Econômica, 1 = Intermediário, 2 = Executivo]: ");
        final var category = scan.nextInt();
        scan.nextLine();
        System.out.print("Ano de fabricação: ");
        final var yearOfManufacture = scan.nextLine();
        System.out.print("Preço: ");
        final var price = scan.nextDouble();
        scan.nextLine();
        try {
            final var content = renavam + "-" + name + "-" + category + "-" + yearOfManufacture + "-" + price;
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(content, vernamKey, aesKey);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var message = new Message(
                    MessageTypes.UPDATE_CAR,
                    secureMessage,
                    rsaSignature,
                    null,
                    RSA_PUBLIC_KEY,
                    RSA_MODULUS,
                    hmacKey,
                    vernamKey,
                    aesKey);
            final var pack = new Package(this.PORT, ImplGatewayRemote.PORT, message);
            final var response = this.proxyReverseStub.execute(pack);
            final var rawContent = sdcStub.decryptMessage(response.content(), response.VERNAM_KEY(), response.AES_KEY());
            final var checkHmac = sdcStub.checkSignMessage(response.HMAC(), response.RSA_PUBLIC_KEY(), response.RSA_MODULUS());
            final var isAuthentic = this.checkingAuthenticity(response.HMAC_KEY(), response.content(), checkHmac);
            if (!isAuthentic) {
                System.out.println("autenticidade inválida vindo do gateway");
                this.login();
            }
            System.out.println(rawContent);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private boolean checkingAuthenticity(String hmacKey, String secureContent, String hmac) {
        try {
            final var hmacCompare = HMAC.hMac(hmacKey, secureContent);
            return hmacCompare.equals(hmac);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] makeMessageSecure(String content) {
        try {
            final var vernamKey = sdcStub.getVernamKey();
            final var aesKey = sdcStub.getAESKey();
            final var hmacKey = sdcStub.getVernamKey();
            final var secureMessage = sdcStub.encryptMessage(content, vernamKey, aesKey);
            System.out.println(secureMessage);
            final var rsaSignature = sdcStub.signMessage(secureMessage, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
            final var output = new String[2];
            output[0] = secureMessage;
            output[1] = rsaSignature;
            return output;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
