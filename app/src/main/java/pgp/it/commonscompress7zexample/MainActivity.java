package pgp.it.commonscompress7zexample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void testList7zContent(File input) {
        if (!input.exists()) {
            Log.e(this.getClass().getCanonicalName(), "testList7zContent: input archive does not exist");
            return;
        }
        SevenZFile _7;
        try {
            _7 = new SevenZFile(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Log.e(getClass().getCanonicalName(), "Listing content for "+input.getAbsolutePath());
        for (SevenZArchiveEntry x : _7.getEntries()) {
            Log.d(this.getClass().getCanonicalName(), "archive entry: "+x.getName());
        }
    }

    public void testCompress() {
        File mainDir = new File(Environment.getExternalStorageDirectory(),"files");
        mainDir.mkdirs();

        File f,g;

        try {
            f = new File(mainDir,"1.txt");
            f.createNewFile();
            g = new File(mainDir,"2.txt");
            g.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File dd= new File(Environment.getExternalStorageDirectory(),"biagio");
        dd.mkdirs();


        File output = new File(Environment.getExternalStorageDirectory(),"CommonsCompressTest.7z");
        if (output.exists()) {
            output.delete();
            Log.d(this.getClass().getCanonicalName(), "testCompress: output file already exists, deleted");
        }
        try {
            SevenZOutputFile sevenZOutput = new SevenZOutputFile(output);

            SevenZArchiveEntry dirEntry = sevenZOutput.createArchiveEntry(dd,"biagio");
            sevenZOutput.putArchiveEntry(dirEntry);
            sevenZOutput.closeArchiveEntry();

            SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(f, "files/1.txt");
            sevenZOutput.putArchiveEntry(entry);
            sevenZOutput.closeArchiveEntry();

            entry = sevenZOutput.createArchiveEntry(g, "files/2.txt");
            sevenZOutput.putArchiveEntry(entry);
            sevenZOutput.closeArchiveEntry();

            sevenZOutput.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        testCompress();

        File justCreatedInput = new File(Environment.getExternalStorageDirectory(),"CommonsCompressTest.7z");
        testList7zContent(justCreatedInput);
        File special7z = new File(Environment.getExternalStorageDirectory(),"1special.7z");
        testList7zContent(special7z);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
