/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hitleapbot;

import static hitleapbot.HitleapBot.username;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reza Elborneo
 */
public class HitleapBotNew {

    String cfduid, hlSession, authToken, hlAuth, csrfToken;
    static String username = "elborneo3";
    static String password = "123456";
    Socket socket;
    OutputStream os;
    InputStream is;
    InetAddress address;
    //String address = "208.48.81.133";
    int port = 80;
    String response;
    int runtime = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //new HitleapBotNew().startHere();
        if (args.length == 2) {
            username = args[0];
            password = args[1];
            new HitleapBotNew().startHere();
        } else {
            System.out.println("Usage : java -jar <file>.jar <username> <password>");
        }

    }

    public void startHere() {
        try {
            address = InetAddress.getByName("hitleap.com");
            socket = new Socket(address, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            String getLogin = "GET http://hitleap.com/log-in HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Connection: keep-alive\r\n"
                    + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Accept-Language: en-us,en\r\n\r\n";
            //+ "Cookie: __cfduid=" + cfduid + "; _hitleap_session=" + hlSession + "\r\n\r\n";

            os.write(getLogin.getBytes());
            response = readResponse(is);
            os.close();
            is.close();
            socket.close();
            storeData(response);

            //login
            Util.log("Logging in with username " + username);
            String data = "utf8=" + URLEncoder.encode("✓", "UTF-8") + "&authenticity_token=" + URLEncoder.encode(authToken, "UTF-8") + "&requested_path=" + URLEncoder.encode("/traffic-exchange", "UTF-8") + "&identifier=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&button=";
            String doLogin = "POST http://hitleap.com/log-in HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Connection: keep-alive\r\n"
                    + "Content-Length: " + data.length() + "\r\n"
                    + "Cache-Control: max-age=0\r\n"
                    + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\r\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "Origin: http://hitleap.com\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Referer: http://hitleap.com/log-in\r\n"
                    + "Accept-Encoding: gzip,deflate\r\n"
                    + "Accept-Language: en-us,en\r\n"
                    + "Cookie: __cfduid=" + cfduid + "; _hitleap_session=" + hlSession + "\r\n"
                    + "\r\n"
                    + data;

            socket = new Socket(address, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(doLogin.getBytes());
            os.close();
            is.close();
            socket.close();
            storeData(response);

            //get start exchange page
            String startExchange = "GET http://hitleap.com/traffic-exchange/start HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Connection: keep-alive\r\n"
                    + "Cache-Control: max-age=0\r\n"
                    + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Referer: http://hitleap.com/log-in\r\n"
                    //+ "Accept-Encoding: gzip,deflate\r\n"
                    + "Accept-Language: en-us,en\r\n"
                    + "Cookie: __cfduid=" + cfduid + "; _hitleap_auth=" + hlAuth + "; _hitleap_session=" + hlSession + "\r\n\r\n";

            socket = new Socket(address, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(startExchange.getBytes());
            os.close();
            is.close();
            socket.close();
            storeData(response);

            //start chunk request
            Socket socketCN = new Socket(address, port);
            getChunkCN(socketCN);

        } catch (IOException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void storeData(String response) throws IOException {
        Util.log("Storing cookies....");

        try {
            cfduid = response.split("__cfduid=")[1].split(";")[0];
            Util.log("cfuid = " + cfduid);
        } catch (Exception e) {
        }
        try {
            hlSession = response.split("_hitleap_session=")[1].split(";")[0];
            Util.log("hlsession = " + hlSession);
        } catch (Exception e) {
        }
        try {
            hlAuth = response.split("_hitleap_auth=")[1].split(";")[0];
            Util.log("hlauth = " + hlAuth);
        } catch (Exception e) {
        }

        try {
            authToken = response.split("name=\"authenticity_token\" value=\"")[1].split("\"")[0];
            Util.log("authtoken = " + authToken);
        } catch (Exception e) {
        }

        try {
            csrfToken = response.split("<meta name=\"csrf-token\" content=\"")[1].split("\" />")[0];
            Util.log("csrftoken = " + csrfToken);
        } catch (Exception e) {
        }
    }

    public void getChunkCN(Socket socket) {
        Util.log("Start chunk cn request...");
        try {
            String chunkCn = "POST http://hitleap.com/get-chunks HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Accept: */*\r\n"
                    + "Cookie: _hitleap_session=" + hlSession + "; _hitleap_auth=" + hlAuth + "\r\n"
                    + "X-CSRF-Token: " + csrfToken + "\r\n"
                    + "X-Requested-With: XMLHttpRequest\r\n"
                    + "Hitleap-Username: " + username + "\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Content-Length: 10\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\r\n"
                    + "\r\n"
                    + "chunks=c,n";

            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(chunkCn.getBytes());

            response = readResponse(is);
            storeData(response);
            try {
                Util.log("SESSION MINUTES EARNED : " + response.split("\"session_minutes_earned\":\"")[1].split("\"}")[0]);
            } catch (Exception e) {
            }
            if (response.contains("Content-Length: 19")) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {

                }
                getChunkCN(socket);
            } else if (response.contains("Content-Length: 41")) {
                Util.log("exchange-session-not-available");
            } else {

                while (true) {

                    try {
                        Thread.sleep(180000);
                    } catch (InterruptedException ex) {

                    }
                    Socket socketNN = new Socket(address, 80);
                    getChunkNN(socketNN);
                    runtime += 3;
                    Util.log("Runtime = " + runtime + "minutes");
                    if (runtime > 20) {
                        runtime = 0;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        logout();
                         try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        startHere();
                    } 

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getChunkNN(Socket socket) {
        Util.log("Start chunk nn request...");
        try {
            String chunkNn = "POST http://hitleap.com/get-chunks HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Accept: */*\r\n"
                    + "Cookie: _hitleap_session=" + hlSession + "; _hitleap_auth=" + hlAuth + "\r\n"
                    + "X-CSRF-Token: " + csrfToken + "\r\n"
                    + "X-Requested-With: XMLHttpRequest\r\n"
                    + "Hitleap-Username: " + username + "\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Content-Length: 9\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\r\n"
                    + "\r\n"
                    + "chunks=nn";

            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(chunkNn.getBytes());

            response = readResponse(is);
            storeData(response);
            try {
                Util.log("SESSION MINUTES EARNED : " + response.split("\"session_minutes_earned\":\"")[1].split("\"}")[0]);
            } catch (Exception e) {
            }

            if (response.contains("Content-Length: 19")) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {

                }
                getChunkNN(socket);
            } else if (response.contains("Content-Length: 41")) {
                Util.log("exchange-session-not-available");
            }

        } catch (IOException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void logout() {
        try {
            //logout
            Util.log("Logging out in with username " + username);
            String data = "_method=post&authenticity_token=" + URLEncoder.encode(authToken, "UTF-8");
            String doLogout = "POST /log-out HTTP/1.1\r\n"
                    + "Host: hitleap.com\r\n"
                    + "Connection: keep-alive\r\n"
                    + "Content-Length: " + data.length() + "\r\n"
                    + "Cache-Control: max-age=0\r\n"
                    + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\n"
                    + "Hitleap-Viewer-Os-Version: 6.2\r\n"
                    + "Hitleap-Viewer-Version: 2.8\r\n"
                    + "Origin: http://hitleap.com\r\n"
                    + "User-Agent: HitLeap Viewer 2.8\r\n"
                    + "Referer: http://hitleap.com/traffic-exchange/start\r\n"
                    + "Accept-Encoding: gzip,deflate\r\n"
                    + "Accept-Language: en-us,en\r\n"
                    + "Cookie: _hitleap_auth="+hlAuth+"; _hitleap_session="+hlSession+";\r\n\r\n"
                    + data;

            socket = new Socket(address, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write(doLogout.getBytes());
            os.close();
            is.close();
            socket.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String readResponse(InputStream is) throws IOException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HitleapBotNew.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ret = null;
        byte[] b = new byte[30000000];
        int a = is.read(b);
        String str = new String(b, 0, a);
        //String data[] = str.split("\r\n\r\n");
        ret = str;
        return ret;

    }

}
