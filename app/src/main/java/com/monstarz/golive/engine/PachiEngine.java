package com.monstarz.golive.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.monstarz.golive.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import lrstudios.games.ego.lib.ExternalGtpEngine;
import lrstudios.games.ego.lib.Utils;
import lrstudios.util.android.AndroidUtils;


public class PachiEngine extends ExternalGtpEngine {

    private static final String TAG = "PachiEngine";

    /**
     * Increment this counter each time you update the Pachi executable (this will force the app to extract it again).
     */
    private static final int EXE_VERSION = 3;

    private static final String ENGINE_NAME = "Pachi";

    private static final String ENGINE_VERSION = "10.00";

    private static final String PREF_KEY_VERSION = "pachi_exe_version";

    private int time = 600;

    private int maxTreeSize = 256;


    public PachiEngine(Context context) {
        super(context);
        long totalRam = AndroidUtils.getTotalRam(context);
        // The amount of RAM used by pachi (adjustable with max_tree_size) should not
        // be too high compared to the total RAM available, because Android can kill a
        // process at any time if it uses too much memory.
        if (totalRam > 0) {
            maxTreeSize = Math.max(256, (int) Math.round(totalRam / 1024.0 / 1024.0 * 0.5));
        }
    }

    @Override
    public boolean init(Properties properties) {
        int level = Utils.tryParseInt(properties.getProperty("level"), 5);
        int boardsize = Utils.tryParseInt(properties.getProperty("boardsize"), 9);

        time = (int) Math.round((boardsize * 1.5) * (0.5 + level / 10.0));

        return super.init(properties);
    }

    @Override
    protected String[] getProcessArgs() {
        Log.v(TAG, "Set max_tree_size = " + maxTreeSize);
        Log.v(TAG, "Set time = " + time);

        return new String[]{"-t", "" + time, "max_tree_size=" + maxTreeSize};
    }

    @Override
    protected File getEngineFile() {
        File dir = _context.getDir("engines", Context.MODE_PRIVATE);
        File file = new File(dir, "pachi");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        int version = prefs.getInt(PREF_KEY_VERSION, 0);
        if (version < EXE_VERSION) {
            if (file.exists()) {
                file.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(file), 4096);
                inputStream = new BufferedInputStream(_context.getResources().openRawResource(R.raw.pachi), 4096);
                Utils.copyStream(inputStream, outputStream, 4096);

                try {
                    //file.setExecutable(true); TODO test this instead of chmod
                    new ProcessBuilder("chmod", "744", file.getAbsolutePath()).start().waitFor();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(PREF_KEY_VERSION, EXE_VERSION);
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) { // TODO handle file extracting errors
                e.printStackTrace();
            } finally {
                Utils.closeObject(inputStream);
                Utils.closeObject(outputStream);
            }
        }

        return file;
    }

    @Override
    public String getName() {
        return ENGINE_NAME;
    }

    @Override
    public String getVersion() {
        return ENGINE_VERSION;
    }
}
