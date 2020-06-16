package com.library.util.common;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * ProjectName: X-Util
 * ClassName: BitmapUtil
 * Author: XG
 * CreateDate: 2020/1/14 23:25
 * Description:Bitmap工具类
 */
public class BitmapUtil {
    private BitmapUtil() {
    }

    public static byte[] bitmapToByte(Bitmap b) {
        return bitmapToByte(b, 100);
    }

    public static byte[] bitmapToByte(Bitmap b, int quality) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, quality, o);
        return o.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] b) {
        return b != null && b.length != 0 ? BitmapFactory.decodeByteArray(b, 0, b.length) : null;
    }

    public static String bitmapToString(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap), 0);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / (float) org.getWidth(), (float) newHeight / (float) org.getHeight());
    }

    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
        }
    }

    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0);
        canvas.drawCircle((float) (width / 2), (float) (height / 2), (float) (width / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle, int newHeight, int newWidth) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        float scaleWidth = (float) newWidth / (float) width;
        float scaleHeight = (float) newHeight / (float) height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        if (needRecycle) {
            bitMap.recycle();
        }

        return newBitMap;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return false;
        } else {
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                boolean var3 = true;
                return var3;
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

            }

            return false;
        }
    }

    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }

    public static Intent buildImageGetIntent(Uri saveTo, int outputX, int outputY, boolean returnData) {
        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData);
    }

    public static Intent buildImageGetIntent(Uri saveTo, int aspectX, int aspectY, int outputX, int outputY, boolean returnData) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < KITKAT) {
            intent.setAction("android.intent.action.GET_CONTENT");
        } else {
            intent.setAction("android.intent.action.OPEN_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
        }

        intent.setType("image/*");
        intent.putExtra("output", saveTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int outputX, int outputY, boolean returnData) {
        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY, int outputX, int outputY, boolean returnData) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", uriTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent buildImageCaptureIntent(Uri uri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        return intent;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
    }

    public static int calculateInSampleSize(int oldWidth, int oldHeight, int reqWidth, int reqHeight) {
        int inSampleSize = 0;
        if (oldHeight > reqHeight || oldWidth > reqWidth) {
            float ratioW = (float) oldWidth / (float) reqWidth;
            float ratioH = (float) oldHeight / (float) reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }

        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

    public static Bitmap decodeBitmap(byte[] data, int inSampleSize) {
        // 调用上面定义的方法计算inSampleSize值
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        // 调用上面定义的方法计算inSampleSize值
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
        byte[] data = bitmapToByte(bitmap);
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeBitmap(Resources res, int resId,
                                      int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 压缩图片，压缩到某个大小以内
     *
     * @param bitmap
     * @param maxImgSize 最大大小
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int maxImgSize) {
        Bitmap bitmap1 = decodeBitmap(bitmap, 480, 800);
        byte[] data = bitmapToByte(bitmap, 100);
        double fileLength = data.length / 1024;
        while (fileLength > maxImgSize) {
            bitmap1 = decodeBitmap(data, 2);
            data = bitmapToByte(bitmap1);
            fileLength = data.length / 1024;
        }
        return bitmap1;
    }

    public byte[] compressBitmap(String filePath, int reqWidth, int reqHeight, int quality) {
        Bitmap bitmap = decodeBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();
        return bytes;
    }

    public byte[] compressBitmap(Bitmap bitmap, int reqWidth, int reqHeight, int quality) {
        Bitmap newBitmap = decodeBitmap(bitmap, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        newBitmap.recycle();
        return bytes;
    }

    public byte[] compressBitmapSmallTo(String filePath, int reqWidth, int reqHeight, int maxLength) {
        int quality = 100;

        byte[] bytes;
        for (bytes = this.compressBitmap(filePath, reqWidth, reqHeight, quality); bytes.length > maxLength && quality > 0; bytes = this.compressBitmap(filePath, reqWidth, reqHeight, quality)) {
            quality /= 2;
        }

        return bytes;
    }

    public byte[] compressBitmapQuikly(String filePath) {
        return this.compressBitmap(filePath, 480, 800, 50);
    }

    public byte[] compressBitmapQuiklySmallTo(String filePath, int maxLenth) {
        return this.compressBitmapSmallTo(filePath, 480, 800, maxLenth);
    }
}
