

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

/**
 *
 * @author Vaio
 */
public class WebParserParallel {

    static private Hashtable<String, Integer> allUrls;
    static private String urlRoot;
    static private int depth;
    static private String output;
    static private int numThreads;

    public static String getHtmlFromUrl(String urlName) {
        String buffer = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlName);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            //BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer += line;
//                writer.write(line);
//                writer.newLine();
            }
            reader.close();
//            writer.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return buffer;
    }

    public static void parseUrls(int depth, String urlName) {
        ArrayList<String> childUrls = new ArrayList<String>();

        if (depth == 1) {
            try {
                URL url = new URL(urlName);
                Reader reader = new InputStreamReader((InputStream) url.getContent());
                HTMLUtils htmlPrser = new HTMLUtils(urlRoot, reader);

                // count the number of the capital letters in the current website
                allUrls.put(urlName, htmlPrser.getNumCapital());
                System.out.println("Tree Leaf - Master Thread: " + urlName + " --> " + allUrls.get(urlName));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else {

            try {
                URL url = new URL(urlName);
                Reader reader = new InputStreamReader((InputStream) url.getContent());
                HTMLUtils htmlPrser = new HTMLUtils(urlRoot, reader);

                // count the number of the capital letters in the current website
                allUrls.put(urlName, htmlPrser.getNumCapital());
                System.out.println("Tree Node - Master Thread: " + urlName + " --> " + allUrls.get(urlName));

                List<String> links = htmlPrser.getListLink();
                for (String link : links) {
                    if (!allUrls.containsKey(link)) {
                        allUrls.put(link, 0);
                        childUrls.add(link);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }


            // Starting concurrency

            SearchHandlerThread[] worker = new SearchHandlerThread[numThreads];

            if (childUrls.size() < numThreads) {
                numThreads = childUrls.size();
            }
            int length = (int) (childUrls.size() / numThreads);

            System.out.println(numThreads + " Worker Thread Created");
            for (int i = 0; i < numThreads; i++) {

                if (i == numThreads - 1) {
                    worker[i] = new SearchHandlerThread(depth - 1, urlRoot, allUrls, childUrls.subList(i * length, childUrls.size()));
                    worker[i].start();
                } else {
                    worker[i] = new SearchHandlerThread(depth - 1, urlRoot, allUrls, childUrls.subList(i * length, ((i + 1) * length)));
                    worker[i].start();
                }
            }
            System.out.print("Wait for worker threads to complete\n");
            for (int i = 0; i < numThreads; i++) {
                try {
                    worker[i].join();
                } catch (InterruptedException e) {
                    System.out.print("Join interrupted\n");
                }
            }
            System.out.print("Worker threads completed\n");

        }
    }  //end of parseUrls

    public static void main(String[] args) {

        urlRoot = "http://bbc.com";
        depth = 2;
        output = "resultParallel.txt";

        // it's defult number of the threads
        numThreads = 20;

        if (args.length == 3 || args.length == 4) {
            urlRoot = args[0];
            urlRoot = urlRoot.toLowerCase(Locale.ENGLISH);
            if (!urlRoot.startsWith("http")) {
                urlRoot = "http://" + urlRoot;
            }
            depth = Integer.valueOf(args[1]);
            output = args[2];
            if (args.length == 4) {
                numThreads = Integer.valueOf(args[3]);
            }
        } else {
            System.out.println("Input Correct parameters !!!");
            return;
        }

        int allCapital = 0;

        allUrls = new Hashtable<String, Integer>();
        allUrls.put(urlRoot, 0);
        parseUrls(depth, urlRoot);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            Enumeration e = allUrls.keys();

            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                writer.write(o.toString() + " --> " + allUrls.get(o));
                allCapital += allUrls.get(o);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //System.out.println("process  " + allUrls.get("sina"));
        System.out.println("Number of all processed Webpages --> " + allUrls.size());
        System.out.println("Number of all capital letters --> " + allCapital);
    }
}

class HTMLUtils {

    private static ArrayList<String> listLink = new ArrayList<String>();
    private static ArrayList<String> listText = new ArrayList<String>();
    private static int numCapital;

    public static int getNumCapital() {
        return numCapital;
    }

    public static ArrayList<String> getListLink() {
        return listLink;
    }

    public static ArrayList<String> getListText() {
        return listText;
    }

    public HTMLUtils(final String urlRoot, Reader reader) throws IOException {
        ParserDelegator parserDelegator = new ParserDelegator();

        ParserCallback parserCallback = new ParserCallback() {

            public void handleText(final char[] data, final int pos) {
                String temp = new String(data);
                listText.add(temp);
                for (int i = temp.length() - 1; i >= 0; i--) {
                    if (Character.isUpperCase(temp.charAt(i))) {
                        numCapital++;
                    }
                }
            }

            public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
                if (tag == Tag.A) {
                    String address = (String) attribute.getAttribute(Attribute.HREF);

                    if (address != null && address.length() > 1) {
                        if (address.startsWith("/")) {
                            listLink.add(urlRoot + address);
                        } else if (address.startsWith("http")) {
                            listLink.add(address);
                        } else if (Character.isLetter(address.charAt(0))) {
                            listLink.add(urlRoot + "/" + address);
                        }
                    }
                }
            }

            public void handleEndTag(Tag t, final int pos) {
            }

            public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) {
            }

            public void handleComment(final char[] data, final int pos) {
            }

            public void handleError(final java.lang.String errMsg, final int pos) {
            }
        };
        parserDelegator.parse(reader, parserCallback, true);
    }
}
