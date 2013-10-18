package com.example.ourfirstproj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("resource")
	public static void copy(File source, File dest) throws IOException {

		FileChannel sourceChannel = new FileInputStream(source).getChannel();
		try {
			FileChannel destChannel = new FileOutputStream(dest).getChannel();
			try {
				destChannel
						.transferFrom(sourceChannel, 0, sourceChannel.size());
			} finally {
				destChannel.close();
			}
		} finally {
			sourceChannel.close();
		}
	}

	String makeNewMp3FileName(File mp3File) throws ID3Exception {
		MediaFile oMediaFile = new MP3File(mp3File);
		ID3Tag[] aoID3Tag = oMediaFile.getTags();
		String title = "";
		String artist = "";
		for (int i = 0; i < aoID3Tag.length; i++) {
			// check to see if we read a v1.0 tag, or a v2.3.0 tag (just for
			// example..)
			if (aoID3Tag[i] instanceof ID3V1_0Tag) {
				ID3V1_0Tag oID3V1_0Tag = (ID3V1_0Tag) aoID3Tag[i];
				// does this tag happen to contain a title?
				if (oID3V1_0Tag.getTitle() != null) {
					title = oID3V1_0Tag.getTitle();
				}
				if (oID3V1_0Tag.getArtist() != null) {
					artist = oID3V1_0Tag.getArtist();
				}
			} else if (aoID3Tag[i] instanceof ID3V2_3_0Tag) {
				ID3V2_3_0Tag oID3V2_3_0Tag = (ID3V2_3_0Tag) aoID3Tag[i];
				if (oID3V2_3_0Tag.getArtist() != null) {
					title = oID3V2_3_0Tag.getTitle();
				}

				if (oID3V2_3_0Tag.getArtist() != null) {
					artist = oID3V2_3_0Tag.getArtist();
				}
			}
		}
		if (artist.isEmpty() && title.isEmpty())
			return "";
		String res = artist + " - " + title;
		return res;
	}

	private static final String allowedChars = "ZXCVBNMLKJHGFDSAQWERTYUIOPzxcvbnmlkjhgfdsaqwertyuiop"
			+ "ˇ˜ÒÏËÚ¸·˛˝Ê‰ÎÓÔ‡‚˚ÙÈˆÛÍÂÌ„¯˘Áı˙∏ﬂ◊—Ã»“‹¡ﬁ›∆ƒÀŒ–œ¿¬€‘…÷” ≈Õ√ÿŸ«’⁄ .!{}[]-_!@#$%^&";

	boolean isNormCoding(String str) {
		int badSymCount = 0;
		for (int i = 0; i < str.length(); i++) {
			if (allowedChars.indexOf(str.charAt(i)) < 0)
				badSymCount++;

		}
		return badSymCount <= str.length() / 2.5;
	}

	public void downloadFiles(View v) {

		File vkAudioCacheDir = new File(
				Environment.getExternalStorageDirectory()
						+ "/.vkontakte/cache/audio/");
		File[] cachedFiles = vkAudioCacheDir.listFiles();

		new File(Environment.getExternalStorageDirectory() + "/Music/fromVK")
				.mkdir();

		text.setText("");
		text.append(cachedFiles.length + " files found!\n");

		for (int i = 0; i < cachedFiles.length; i++) {
			try {

				String oldFileName = cachedFiles[i].toString();
				if (oldFileName.contains(".cover"))
					continue;

				String newFileName = makeNewMp3FileName(cachedFiles[i]);

				if (newFileName.isEmpty() || !isNormCoding(newFileName)) {
					newFileName = oldFileName.substring(oldFileName
							.lastIndexOf("/") + 1);
				}

				newFileName = newFileName.replaceAll("['*:|?<>\\\\/]", "")
						.trim();

				if (!newFileName.contains(".mp3"))
					newFileName += ".mp3";

				String fullName = Environment.getExternalStorageDirectory()
						+ "/Music/fromVK/" + newFileName;
				File newFile = new File(fullName);

				if (!newFile.exists()) {
					newFile.createNewFile();
				} else {
					text.append("* " + newFileName + " already exists!\n");
					continue;
				}

				copy(cachedFiles[i], newFile);

				text.append("* File #" + (i + 1) + ": " + newFileName
						+ " copied success!\n");
				v.invalidate();

				cachedFiles[i].delete();

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.toString(),
						Toast.LENGTH_LONG).show();
			}

		}
		if (cachedFiles != null)
			Toast.makeText(getApplicationContext(),
					"œÓÒÍ‡ÌËÓ‚‡ÌÓ Ù‡ÈÎÓ‚: " + cachedFiles.length,
					Toast.LENGTH_LONG).show();
	}

}
