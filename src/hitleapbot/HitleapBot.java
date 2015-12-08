/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hitleapbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reza Elborneo
 */
public class HitleapBot {

    String cfduid, hlSession, authToken, hlAuth, csrfToken;
    static String username = "elborneo1";
    static String password = "123456";
    Socket socket;
    OutputStream os;
    InputStream is;
    InetAddress address;
    //String address = "208.48.81.133";
    int port = 80;

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        
//        new HitleapBot().startHere();
//        if (args.length == 2) {
//            username = args[0];
//            password = args[1];
//            new HitleapBot().startHere();
//        } else {
//            System.out.println("Usage : java -jar <file>.jar <username> <password>");
//        }
//
//    }

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
            storeCookie(socket);
            getAuthToken(socket);
  

            System.out.println("Logging in with username " + username);
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
            storeCookie(socket);

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
            storeCookie(socket);
            getCsrfToken(socket);

            //chunkcn
            Socket socketCN = new Socket(address, port);
            getChunkCN(socketCN);

        } catch (IOException ex) {
            Logger.getLogger(HitleapBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void storeCookie(Socket socket) throws IOException {
        System.out.println("Storing cookies....");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            if (line.isEmpty()) {
                break; // Stop when headers are completed. We're not interested in all the HTML.
            }
            if (line.startsWith("Set-Cookie:")) {
                if (line.contains("__cfduid")) {
                    cfduid = line.split("__cfduid=")[1].split(";")[0];
                    System.out.println("__cfduid = " + cfduid);

                }
                if (line.contains("_hitleap_session")) {
                    hlSession = line.split("_hitleap_session=")[1].split(";")[0];
                    System.out.println("_hitleap_session = " + hlSession);
                }

                if (line.contains("_hitleap_auth")) {
                    hlAuth = line.split("_hitleap_auth=")[1].split(";")[0];
                    System.out.println("_hitleap_auth = " + hlAuth);
                }
            }
        }

    }

    public void getAuthToken(Socket socket) throws IOException {
        System.out.println("Getting auth token....");
        StringBuilder pageSource = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String l = "";
        while ((l = br.readLine()) != null) {
            if (l.startsWith("</form>")) {
                break;
            }
            pageSource.append(l);
        }

        //System.out.println(pageSource.substring(pageSource.indexOf("name=\"authenticity_token\" value=\"")+33,pageSource.indexOf(">",1)));
        authToken = pageSource.toString().split("value=\"")[2].split("\" />")[0];
    }

    public void getCsrfToken(Socket socket) throws IOException {
        System.out.println("Getting csrf token....");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String l = "";
        while ((l = br.readLine()) != null) {
            if (l.contains("csrf-token")) {
                csrfToken = l.split("<meta name=\"csrf-token\" content=\"")[1].split("\" />")[0];
                System.out.println("csrfToken = " + csrfToken);
                break;
            }

        }

    }

    public void getChunkCN(Socket socket) {
        System.out.println("Start chunk cn request...");
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

            String contentLength = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for (String line; (line = reader.readLine()) != null;) {
                if (line.isEmpty()) {
                    break; // Stop when headers are completed. We're not interested in all the HTML.
                }
                if (line.startsWith("Set-Cookie:")) {
                    if (line.contains("__cfduid")) {
                        cfduid = line.split("__cfduid=")[1].split(";")[0];
                        System.out.println("__cfduid = " + cfduid);
                    }
                    if (line.contains("_hitleap_session")) {
                        hlSession = line.split("_hitleap_session=")[1].split(";")[0];
                        System.out.println("_hitleap_session = " + hlSession);
                    }

                    if (line.contains("_hitleap_auth")) {
                        hlAuth = line.split("_hitleap_auth=")[1].split(";")[0];
                        System.out.println("_hitleap_auth = " + hlAuth);
                    }
                }
                if (line.startsWith("Content-Length")) {
                    contentLength = line;
                }

            }

            if (contentLength.equals("Content-Length: 19")) {
                getChunkCN(socket);
            } else if (contentLength.equals("Content-Length: 41")) {
                System.out.println("exchange-session-not-available");
            } else {

                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {

                    }
                    Socket socketNN = new Socket(address, 80);
                    getChunkNN(socketNN);

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(HitleapBot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getChunkNN(Socket socket) {
        System.out.println("Start chunk nn request...");
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

            String contentLength = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for (String line; (line = reader.readLine()) != null;) {
                if (line.isEmpty()) {
                    break; // Stop when headers are completed. We're not interested in all the HTML.
                }
                if (line.startsWith("Set-Cookie:")) {
                    if (line.contains("__cfduid")) {
                        cfduid = line.split("__cfduid=")[1].split(";")[0];
                        System.out.println("__cfduid = " + cfduid);
                    }
                    if (line.contains("_hitleap_session")) {
                        hlSession = line.split("_hitleap_session=")[1].split(";")[0];
                        System.out.println("_hitleap_session = " + hlSession);
                    }

                    if (line.contains("_hitleap_auth")) {
                        hlAuth = line.split("_hitleap_auth=")[1].split(";")[0];
                        System.out.println("_hitleap_auth = " + hlAuth);
                    }
                }
                if (line.startsWith("Content-Length")) {
                    contentLength = line;
                }

            }

            if (contentLength.equals("Content-Length: 19")) {
                getChunkNN(socket);
            }

        } catch (IOException ex) {
            Logger.getLogger(HitleapBot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String ambilHeader(InputStream is) throws IOException {

        String ret = null;
        byte[] b = new byte[2048];
        int a = is.read(b);
        String str = new String(b, 0, a);
        String data[] = str.split("\r\n\r\n");
        ret = data[0];
        System.out.println(data[0]);
        return ret;

    }

}
