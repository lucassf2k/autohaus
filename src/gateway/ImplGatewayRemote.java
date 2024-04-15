package gateway;

import authentication.AuthenticationRemote;
import authentication.ImplAuthenticationRemote;
import authentication.User;
import authentication.UserTypes;
import crypto.HMAC;
import database.DatabaseRemote;
import sdc.ImplSdcService;
import sdc.SdcService;
import shared.Car;
import shared.CarCategories;
import shared.Message;

import javax.security.auth.login.CredentialNotFoundException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class ImplGatewayRemote implements GatewayRemote {
    public static final int PORT = 20006;
    private final AuthenticationRemote authenticationStub;
    private final DatabaseRemote carDatabaseStub;
    private final BigInteger RSA_PUBLIC_KEY;
    private final BigInteger RSA_PRIVATE_KEY;
    private final BigInteger RSA_MODULUS;
    private final SdcService sdcStub;

    public ImplGatewayRemote(AuthenticationRemote authenticationStub, DatabaseRemote carDatabaseStub) throws RemoteException, NotBoundException {
        this.authenticationStub = authenticationStub;
        this.carDatabaseStub = carDatabaseStub;
        final var registrySDC = LocateRegistry.getRegistry(ImplSdcService.PORT);
        sdcStub = (SdcService) registrySDC.lookup("sdc");
        final var rsaKeys = sdcStub.getRSAKeys();
        RSA_PUBLIC_KEY = rsaKeys.publicKey();
        RSA_PRIVATE_KEY = rsaKeys.privateKey();
        RSA_MODULUS = rsaKeys.modulus();
    }

    @Override
    public ImplAuthenticationRemote.Credentials login(String email, String password) throws RemoteException {
        System.out.println("enviando ao serviço de autenticação...");
        return authenticationStub.login(email, password);
    }

    @Override
    public Boolean createCar(String renavam, String name, CarCategories category, String yearManufacture, Double price) throws RemoteException {
        System.out.println("enviando ao serviço de banco de dados...");
        final var newCar = new Car(renavam, name, category, yearManufacture, price);
        return carDatabaseStub.save(newCar);
    }

    @Override
    public List<Car> list() throws RemoteException {
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.list();
    }

    @Override
    public Boolean deleteCar(String renanam) throws RemoteException {
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.delele(renanam);
    }

    @Override
    public Car getCar(String renavam) throws RemoteException {
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.get(renavam);
    }

    @Override
    public List<Car> getCarOfName(String name) throws RemoteException {
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.getOfName(name);
    }

    @Override
    public List<Car> getCarOfCategory(int category) throws RemoteException {
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
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.getOfCategory(carCategory);
    }

    @Override
    public Boolean updateCar(String renavam, String name, CarCategories category, String yearManufacture, Double price) throws RemoteException {
        final var updatedCar = new Car(renavam, name, category, yearManufacture, price);
        System.out.println("enviando ao serviço de banco de dados...");
        return carDatabaseStub.update(renavam, updatedCar);
    }

    @Override
    public Car buyCar(String renavam, double price) throws RemoteException {
        final var carToBuy = getCar(renavam);
        if (!carToBuy.getPrice().equals(price)) return null;
        System.out.println("enviando ao serviço de banco de dados...");
        deleteCar(renavam);
        return carToBuy;
    }

    @Override
    public Response execute(Message message) throws RemoteException {
        String content = "";
        final var hmacMessage = sdcStub
                .checkSignMessage(
                        message.getContent(),
                        message.getHMAC_KEY(),
                        message.getRSA_PUBLIC_KEY(),
                        message.getRSA_MODULUS());
        final var compareSign = sdcStub.signMessage(
                message.getContent(),
                message.getHMAC_KEY(),
                message.getRSA_PUBLIC_KEY(),
                message.getRSA_MODULUS());
        if (!compareSign.equals(hmacMessage)) {
            System.out.println("Autenticação da mensagem inválida");
            return new Response(null, null, null, null, null, null, null);
        }
        switch (message.getType()) {
            case LOGIN: {
                final var rawMessage = sdcStub.decryptMessage(message.getContent(), message.getVERNAM_KEY(), message.getAES_KEY());
                final var loginMessage = rawMessage.split("-");
                final var output = this.login(loginMessage[0], loginMessage[1]);
                content = output.isRegistered() + "-" + output.useType().name();
                break;
            }
            case LIST_CAR: {
                this.list();
            }
        }
        final var vernamKey = sdcStub.getVernamKey();
        final var aesKey = sdcStub.getAESKey();
        final var secureContent = sdcStub.encryptMessage(content, vernamKey, aesKey);
        final var hmacKey = sdcStub.getVernamKey();
        final var hmacSecureContent = sdcStub.signMessage(secureContent, hmacKey, RSA_PRIVATE_KEY, RSA_MODULUS);
        return new Response(RSA_PUBLIC_KEY, RSA_MODULUS, secureContent, hmacSecureContent, hmacKey, vernamKey, aesKey);
    }
}
