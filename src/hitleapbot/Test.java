/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hitleapbot;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reza Elborneo
 */
public class Test {

    public static void main(String[] args) {
        String s = "<link rel=\"apple-touch-icon\" type=\"image/x-icon\" href=\"/assets/apple-touch-icon-iphone-4ca905344182822bc011ca2ccf02510374c071cf145523a648458158bf8f3246.png\" sizes=\"57x57\" />\n"
                + "<meta name=\"csrf-param\" content=\"authenticity_token\" />\n"
                + "<meta name=\"csrf-token\" content=\"SE23XRLH1sy8RDagzsZwojRDYAVOaaOqlbMjuG9wwE4cLcvVcaWBA3sZVDumIFR4ur8Jrv5y8gtRYu3Y4F5f/w==\" />";
        System.out.println(s.split("<meta name=\"csrf-token\" content=\"")[1].split("\" />")[0]);

    }

}
