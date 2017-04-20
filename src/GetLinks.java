
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class GetLinks {

    Vector<String> v = new Vector<>();
    private String link;

    public GetLinks(String link) throws IOException, InterruptedException {
        if (verifyUrl(link)) {
            String var = String.valueOf(link.charAt((link.length() - 1)));
            if (var.equals("/")) {
                this.link = link;
            } else {
                this.link = link + "/";
            }

            try {
                for (int i = Integer.parseInt(AnaJframe.startfield.getText()); i <= Integer.parseInt(AnaJframe.stopfield.getText()); i++) {
                    Document doc = Jsoup.connect(this.link + "page/" + i).get();
                    Elements sLink = doc.select("img");
                    int elems = sLink.size();

                    for (int j = 0; j < sLink.size(); j++) {
                        String next_url = sLink.get(j).attr("src");
                        v.add(next_url);
                    }

                    addTable();
                    AnaJframe.logtext.append(this.link + "page/" + i + " Added\n");
                    Thread.sleep(2000);

                    if (Integer.parseInt(AnaJframe.stopfield.getText()) == i) {
                        JOptionPane.showMessageDialog(null, "Tüm Resimler Bulundu", "Bilgi", 1);
                        AnaJframe.logtext.append("Finish");
                        AnaJframe.jButton2.setEnabled(false);
                        break;
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Tüm Resimler Bulundu", "Bilgi", 1);
                AnaJframe.logtext.append("Finish");
                AnaJframe.jButton2.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Link Hatalı Lütfen Kontrol Ediniz \n Örnek: http://tumblr.com/sabri", "Dikkat", 0);
        }
        AnaJframe.jButton1.setEnabled(true);
        AnaJframe.DownloadStartBtn.setEnabled(true);
        System.gc();

    }

    private boolean verifyUrl(String url) {
        String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher m = pattern.matcher(url);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void addTable() throws MalformedURLException {

        for (int i = 0; i < v.size(); i++) {

            if (v.get(i).contains("tumblr.com") && !v.get(i).contains("avatar") && !v.get(i).contains("px.srvcs") && !v.get(i).contains("secure")) {

                if (v.get(i).contains("_1280") || v.get(i).contains("_500") || v.get(i).contains("_700") || v.get(i).contains("_640") || v.get(i).contains("_1024") || v.get(i).contains("_540") || v.get(i).contains("_810") || v.get(i).contains("_1920")) {
                    DefaultTableModel model = (DefaultTableModel) AnaJframe.linktablosu.getModel();
                    model.addRow(new Object[]{this.v.get(i), "Bekliyor"});
                    AnaJframe.DownloadStartBtn.setText("İNDİRMEYE BAŞLA (" + AnaJframe.linktablosu.getRowCount() + " Resim)");
                }
            }
        }
        v.removeAllElements();
    }
}
