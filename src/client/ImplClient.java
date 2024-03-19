package client;

import authentication.ImplAuthenticationRemote;
import authentication.UserTypes;
import gateway.GatewayRemote;
import gateway.ImplGatewayRemote;
import shared.CarCategories;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Objects;
import java.util.Scanner;

public class ImplClient {
    private final GatewayRemote gatewayStub;
    private static final Scanner scan = new Scanner(System.in);

    public ImplClient() throws RemoteException, NotBoundException {
        final var registry = LocateRegistry.getRegistry(ImplGatewayRemote.PORT);
        this.gatewayStub = (GatewayRemote) registry.lookup("gateway");
        exec();
    }

    private void exec() {
        try {
            final var isUserValid = this.login();
            this.menu(isUserValid);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private ImplAuthenticationRemote.Credentials login() throws RemoteException {
        Boolean isLogged = Boolean.FALSE;
        ImplAuthenticationRemote.Credentials credentials = null;
        while (!isLogged) {
            System.out.println(" === Bem vindo === ");
            System.out.print("Informe seu email: ");
            final var email = scan.nextLine();
            System.out.print("Informe sua senha: ");
            final var password = scan.nextLine();
            credentials = this.gatewayStub.login(email, password);
            if (credentials.isRegistered()) {
                isLogged = Boolean.TRUE;
            } else {
                System.out.println("login incorreto!");
            }
        }
        return credentials;
    }

    private void menu(ImplAuthenticationRemote.Credentials user) {
        int option = 10;
        System.out.println("== O que quer fazer? ==");
        if (user.useType().equals(UserTypes.CUSTOMER)) {
            // listar, pesquisar, exibir quantidade de carros e comprar
            while (option != 0) {
                System.out.println("""
                        1 - Listar carros
                        2 - Pesquisar carro
                        3 - Quantidade de carros
                        4 - Comprar carro
                        
                        Digite "0" para fechar o sistema
                        """);
                System.out.print("Opção: ");
                option = scan.nextInt();
                switch (option) {
                    case 0: {
                        option = 0;
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
                }
            }
        } else {
            //  listar, pesquisar, exibir quantidade de carros, comprar, apagar e atualizar
            while (option != 0) {
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
                option = scan.nextInt();
                switch (option) {
                    case 0: {
                        option = 0;
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
                }
            }
        }
    }

    private void listCar() {
        try {
            final var cars = this.gatewayStub.list();
            if (cars.isEmpty()) System.out.println("Nenhum carro disponível");
            else cars.forEach(car -> System.out.println(car.toString()));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void searchCar() {
        try {
            System.out.println("""
                    \t == escolha o tipo da pesquisa ==
                    \t 1 - Por Renavam
                    \t 2 - Por Nome
                    \t 3 - Por Categoria
                    """);
            final var option = scan.nextInt();
            switch (option) {
                case 1: {
                    System.out.println("\t Renavam: ");
                    final var renavam = scan.nextLine();
                    final var car = this.gatewayStub.getCar(renavam);
                    if (Objects.isNull(car)) System.out.println("Nenhuma carro com esse renavam!");
                    else System.out.println(car.toString());
                    break;
                }
                case 2: {
                    System.out.println("\t Nome: ");
                    final var name = scan.nextLine();
                    final var car = this.gatewayStub.getCarOfName(name);
                    if (Objects.isNull(car)) System.out.println("Nenhuma carro com esse nome!");
                    else System.out.println(car.toString());
                    break;
                }
                case 3: {
                    System.out.println("""
                            \t 0 - Econômica
                            \t 1 - Intermediário
                            \t 2 - Executivo
                            \t Categoria:
                            """);
                    final var category = scan.nextInt();
                    final var cars = this.gatewayStub.getCarOfCategory(category);
                    if (Objects.isNull(cars)) System.out.println("Nenhuma carro com essa categoria!");
                    else cars.forEach(car -> System.out.println(car.toString()));
                    break;
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void numberOfCars() {
        try {
            final var cars = this.gatewayStub.list();
            System.out.println("Total de carros: " + cars.size());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void buyCar() {
        System.out.println("Renavam: ");
        try {
            final var renavam = scan.nextLine();
            final var car = this.gatewayStub.getCar(renavam);
            if (Objects.isNull(car)) {
                System.out.println("Nenhum carro com esse renavam!");
                return;
            }
            System.out.println(car.toString());
            final var purchasedCar = this.gatewayStub.buyCar(renavam, car.getPrice());
            if (Objects.isNull(purchasedCar)) System.out.println("Pix não foi! Faça novamente!");
            else System.out.println("Carro comprado com sucesso!");
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
        System.out.print("Ano de fabricação: ");
        final var yearOfManufacture = scan.nextLine();
        System.out.print("Preço: ");
        final var price = scan.nextDouble();
        CarCategories carCategory = null;
        if (category == 0) {
            carCategory = CarCategories.ECONOMIC;
        }
        if (category == 1) {
            carCategory = CarCategories.INTERMEDIATE;
        }
        if (category == 2) {
            carCategory = CarCategories.EXECUTIVE;
        }
        try {
            final var isSucess = this.gatewayStub.createCar(renavam, name, carCategory, yearOfManufacture, price);
            if (!isSucess) System.out.println("Cadastro não funcionou! Tente novamente. OBS.: Já pode existir esse renavam");
            else System.out.println("Carro cadastrado com sucesso.");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    private void removeCar() {
        System.out.print("Renavam: ");
        final var renavam = scan.nextLine();
        try {
            final var sucess = this.gatewayStub.deleteCar(renavam);
            if (!sucess) System.out.println("Não foi possível remover esse carro! Verifique o renavam.");
            else System.out.println("Carro com renavam: " + renavam + " apagado com sucesso.");
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
        System.out.print("Ano de fabricação: ");
        final var yearOfManufacture = scan.nextLine();
        System.out.print("Preço: ");
        final var price = scan.nextDouble();
        CarCategories carCategory = null;
        if (category == 0) {
            carCategory = CarCategories.ECONOMIC;
        }
        if (category == 1) {
            carCategory = CarCategories.INTERMEDIATE;
        }
        if (category == 2) {
            carCategory = CarCategories.EXECUTIVE;
        }
        try {
            final var isSucess = this.gatewayStub.updateCar(renavam, name, carCategory, yearOfManufacture, price);
            if (!isSucess) System.out.println("Não foi possível atualizar! Verique o renavam.");
            else System.out.println("Carro atualizado com sucesso.");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }
}
