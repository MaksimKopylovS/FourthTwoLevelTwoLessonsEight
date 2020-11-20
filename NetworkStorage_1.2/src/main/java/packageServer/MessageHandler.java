package packageServer;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import packageBase.AuthService;
import packageBase.BaseAuthService;
import packageMessage.FileMessage;
import packageMessage.MyMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler {

    private final String rootPath = "Server";
    private static String userPath = "NewFolder";
    private AuthService authService;
    private Server server;
    private ClientHandler clientHandler;
    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class);


    public MessageHandler(ChannelHandlerContext ctx, Object msg, ClientHandler clientHandler) {
        this.clientHandler = clientHandler;

        if (!Files.exists(Paths.get(rootPath))) {
            try {
                Files.createDirectory(Paths.get(rootPath));
            } catch (IOException ioException) {
                LOGGER.log(Level.INFO, ioException.getMessage());
                ioException.printStackTrace();
            }
        }


        authService = new BaseAuthService();
        //System.out.println(msg.getClass().getName());
        try {

            if (msg instanceof MyMessage) {
                String comand = ((MyMessage) msg).getText();

                if (comand.startsWith("/")) {
                    LOGGER.log(Level.INFO, "Cистемное от " + clientHandler.getClientName() + ":" + msg + " ");
                    System.out.println("Cистемное от " + clientHandler.getClientName() + ":" + msg + " ");
                    //Аутентификация
                    if (comand.startsWith("/auth")) {
                        if (comand.split(" ", 4).length >= 3) {
                            if (BaseAuthService.busyLogin(comand.split(" ", 4)[1])) {
                                if (!BaseAuthService.authLoginPass(comand.split(" ", 4)[1], comand.split(" ", 4)[2]).equals(null)) {
                                    String strLogin = BaseAuthService.authLoginPass(comand.split(" ", 4)[1], comand.split(" ", 4)[2]);
                                    if (clientHandler.checName(strLogin)) {
                                        clientHandler.setClientName(strLogin);
                                        clientHandler.setListName(strLogin);
                                        clientHandler.saveChannel(ctx);
                                        ctx.writeAndFlush("/name " + clientHandler.getClientName() + " Авторизировался");

                                        userPath = clientHandler.getClientName();
                                        if (Files.exists(Paths.get(rootPath + "/" + userPath))) {
                                            LOGGER.log(Level.INFO, "Дериктория " + userPath + " существует");
                                            System.out.println("Дериктория " + userPath + " существует");
                                        } else {
                                            Files.createDirectory(Paths.get(rootPath + "/" + userPath));
                                        }
                                    } else {
                                        LOGGER.log(Level.INFO, "/auth Пользователь в сети");
                                        ctx.writeAndFlush("/auth Пользователь в сети");
                                    }
                                } else {
                                    LOGGER.log(Level.INFO, "/auth Не верный логи или пароль");
                                    ctx.writeAndFlush("/auth Не верный логи или пароль");
                                }

                            } else {
                                LOGGER.log(Level.INFO, "/auth " + comand.split(" ", 4)[1] + " не существует");
                                ctx.writeAndFlush("/auth " + comand.split(" ", 4)[1] + " не существует");
                            }
                        }
                    }

                    //Регистрация
                    if (comand.startsWith("/reg")) {
                        if (comand.split(" ", 4).length >= 3) {
                            if (BaseAuthService.busyLogin(comand.split(" ", 4)[1])) {
                                LOGGER.log(Level.INFO, "/reg Login " + comand.split(" ", 4)[1] + " занят");
                                ctx.writeAndFlush("/reg Login " + comand.split(" ", 4)[1] + " занят");
                            } else {
                                LOGGER.log(Level.INFO, comand.split(" ", 4)[1] + " " + comand.split(" ", 4)[2]);
                                LOGGER.log(Level.INFO, "/reg Регистрация прошла успешно");
                                System.out.println(comand.split(" ", 4)[1] + " " + comand.split(" ", 4)[2]);
                                authService.registrationBase(comand.split(" ", 4)[1], comand.split(" ", 4)[2]);
                                ctx.writeAndFlush("/reg Регистрация прошла успешно");
                            }
                        }
                    }

                    //  touch (name) создать текстовый файл с именем
                    if (comand.startsWith("/touch")) {
                        System.out.println(userPath + "/" + comand.split(" ", 10)[1]);
                        Path path = Paths.get(rootPath + "/" + userPath + "/" + comand.split(" ", 10)[1]);
                        if (!Files.exists(path)) {
                            Files.createFile(path);
                            LOGGER.log(Level.INFO, path + " Создан");
                            ctx.writeAndFlush(path + " Создан");
                        } else {
                            LOGGER.log(Level.INFO, path + " Существует");
                            ctx.writeAndFlush(path + " Существует");
                        }
                    }
                    //  mkdir (name) создать директорию
                    if (comand.startsWith("/mkdir")) {
                        Path path = Paths.get(rootPath + "/" + userPath + "/" + comand.split(" ", 10)[1]);
                        //Path newDir = Files.createDirectory(path);
                        if (!Files.exists(path)) {
                            Files.createDirectory(path);
                            LOGGER.log(Level.INFO, path + " Создан");
                            ctx.writeAndFlush(path + " Создан");

                        } else {
                            LOGGER.log(Level.INFO, path + " не создан");
                            ctx.writeAndFlush(path + " не создан");
                        }
                    }
                    //  cd (name) - перейти в папку
                    if (comand.startsWith("/cd")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(rootPath + "/" + comand.split(" ", 4)[1]))) {
                                System.out.println(comand.split(" ", 10)[1]);
                                setUserPath(comand.split(" ", 10)[1]);
                                ctx.writeAndFlush("вы в дериктории " + Paths.get(comand.split(" ", 4)[1]));
                            } else {
                                LOGGER.log(Level.INFO, "Нет такого пути");
                                ctx.writeAndFlush("Нет такого пути");
                            }
                        } else {
                            LOGGER.log(Level.INFO, "Ошибка ввода");
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }
                    //  rm (name) удалить файл по имени
                    if (comand.startsWith("/rm")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1]))) {
                                Files.delete(Paths.get(comand.split(" ", 4)[1]));
                                LOGGER.log(Level.INFO, "Файл " + Paths.get(comand.split(" ", 10)[1]) + " удалён");
                                ctx.writeAndFlush("Файл " + Paths.get(comand.split(" ", 10)[1]) + " удалён");
                            } else {
                                ctx.writeAndFlush("Файл не существует");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }

                    }
                    //  copy (src, target) скопировать файл из одного пути в другой
                    if (comand.startsWith("/copy")) {
                        if (comand.split(" ", 4).length >= 3) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1])) || Files.exists(Paths.get(comand.split(" ", 4)[2]))) {
                                Files.copy(Paths.get(comand.split(" ", 10)[1]),
                                        Paths.get(comand.split(" ", 10)[2]));
                                LOGGER.log(Level.INFO, "Файл " + Paths.get(comand.split(" ", 10)[1]));
                                ctx.writeAndFlush("Файл " + Paths.get(comand.split(" ", 10)[1])
                                        + " скопирован" + " в "
                                        + Paths.get(comand.split(" ", 10)[2]));
                                ///copy Server/NewFolder/test.txt Server/test.txt
                            } else {
                                ctx.writeAndFlush("Деректории не существует");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }
                    //  cat (name) - вывести в консоль содержимое файла
                    if (comand.startsWith("/cat")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1]))) {
                                List<String> list = Files.readAllLines(Paths.get(comand.split(" ", 10)[1]));
                                for (String str : list) {
                                    ctx.writeAndFlush(str + "\n");
                                }
                            } else {
                                ctx.writeAndFlush("Файла не сушествует");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }

                    }

                    if (comand.startsWith("/ls")) {
                        Object o = getFilesList();
                        ctx.writeAndFlush(getFilesList());
                    }

                    if (comand.startsWith("/info")) {
                        if (comand.split(" ", 4).length >= 2) {
                            System.out.println(comand);
                            File file = new File(rootPath + "/" + comand.split(" ", 10)[1]);
                            Object o = null;
                            if (file.exists()) {
                                recFind(rootPath + "/" + comand.split(" ", 10)[1], ctx, o);

//                                for (File item : file.listFiles()) {
//
//                                    o = (String) item.getPath().substring(6);
//                                    ctx.writeAndFlush(o);
//                                }
                            } else {
                                ctx.writeAndFlush("Файл не найден");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }


                    if (comand.startsWith("/download")) {
                        try {
                            if (comand.split(" ", 4).length >= 2) {
                                Path path = Paths.get(rootPath + "/" + userPath + "/" + comand.split(" ", 4)[1]);
                                if (Files.exists(path)) {

                                    System.out.println(path.getFileName());
                                    Object o = new FileMessage(path);
                                    ctx.writeAndFlush(o);
                                } else {
                                    ctx.writeAndFlush("Файл не существует");
                                }
                            } else {
                                ctx.writeAndFlush("Ошибка ввода");
                            }

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    if (comand.startsWith("/help")) {
                        String help =
                                "\n/touch name - создать текстовый файл с именем\n"
                                        + "/mkdir name - создать директорию\n"
                                        + "/cd name - перейти в папку\n"
                                        + "/rm name - удалить файл по имени\n"
                                        + "/copy src target скопировать файл из одного пути в другой\n"
                                        + "/cat (name) - вывести в консоль содержимое файла\n"
                                        + "/help - помошь\n"
                                        + "/download - загрузка файла, пример: /download Server/s.jpg\n"
                                        + "/clear - очистить окно\n"
                                        + "/info - что находится в дериктории пример /info Server\n";
                        Object o = help;
                        ctx.writeAndFlush(o);
                    }

                    if (comand.startsWith("/whoodir")) {
                        ctx.writeAndFlush(userPath);
                    }

                } else {
                    String out = ((MyMessage) msg).getText();
                    System.out.print(out + "\n");
                    ctx.writeAndFlush(out + "\n");
                }
            } else {
                System.out.printf("Server recived wrong object");
            }

            if (msg instanceof FileMessage) {
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get("Server/" + userPath + "/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                System.out.println("Client send File name: " + ((FileMessage) msg).getFileName());
                LOGGER.log(Level.INFO, "Сервер записал файл " + fm.getFileName());
                ctx.writeAndFlush("Сервер записал файл " + fm.getFileName());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ctx.writeAndFlush("Ошибка ввода");
        }

    }


    private String getFilesList() {
        return String.join(" ", new File(userPath).list());
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    static void recFind(String szDir, ChannelHandlerContext ctx, Object o) {
        File f = new File(szDir);
        String[] sDirList = f.list();
        int i;
        for (i = 0; i < sDirList.length; i++) {
            File f1 = new File(szDir +
                    File.separator + sDirList[i]);

            if (f1.isFile()) {
                o = (szDir + File.separator + sDirList[i]).substring(7);
                ctx.writeAndFlush(o);
            } else {
                recFind(szDir + File.separator + sDirList[i], ctx, o);
            }
        }
    }


}
