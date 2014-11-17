package com.bldj.lexiang.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

import java.io.FileDescriptor;
import java.io.InputStream;

/**
 * Created by z on 14-2-21.
 */
public class NinePatchDrawableFactory {
    public static final String TAG = NinePatchDrawableFactory.class.getSimpleName();
    /**
     * Decode a file path into a NinePatchDrawable. If the specified file name is null,
     * or cannot be decoded into a NinePatchDrawable, the function returns null.
     *
     * @param pathName complete path name for the file to be decoded.
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return The decoded NinePatchDrawable, or null if the image data could not be
     *         decoded, or, if opts is non-null, if opts requested only the
     *         size be returned (in opts.outWidth and opts.outHeight)
     */
    public static NinePatchDrawable decodeFile(String pathName, Options opts){
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        return createNinePatchDrawable(null, bitmap);
    }
    /**
     * Decode a file path into a NinePatchDrawable. If the specified file name is null,
     * or cannot be decoded into a NinePatchDrawable, the function returns null.
     *
     * @param pathName complete path name for the file to be decoded.
     * @return the resulting decoded NinePatchDrawable, or null if it could not be decoded.
     */
    public static NinePatchDrawable decodeFile(String pathName) {
        return decodeFile(pathName, null);
    }
    /**
     * Decode a new NinePatchDrawable from an InputStream. This InputStream was obtained from
     * resources, which we pass to be able to scale the NinePatchDrawable accordingly.
     */
    public static NinePatchDrawable decodeResourceStream(Resources res, TypedValue value,
                                                         InputStream is, Rect pad, Options opts) {
        Bitmap bitmap = BitmapFactory.decodeResourceStream(res, value, is, pad, opts);
        return createNinePatchDrawable(res, bitmap);
    }
    /**
     * Synonym for opening the given resource and calling
     * {@link #decodeResourceStream}.
     *
     * @param res   The resources object containing the image data
     * @param id The resource id of the image data
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return The decoded NinePatchDrawable, or null if the image data could not be
     *         decoded, or, if opts is non-null, if opts requested only the
     *         size be returned (in opts.outWidth and opts.outHeight)
     */
    public static NinePatchDrawable decodeResource(Resources res, int id, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, opts);
        return createNinePatchDrawable(res, bitmap);
    }
    /**
     *
     * will null Options.
     *
     * @param res The resources object containing the image data
     * @param id The resource id of the image data
     * @return The decoded NinePatchDrawable, or null if the image could not be decode.
     */
    public static NinePatchDrawable decodeResource(Resources res, int id) {
        return decodeResource(res, id, null);
    }
    /**
     * Decode an immutable NinePatchDrawable from the specified byte array.
     *
     * @param data byte array of compressed image data
     * @param offset offset into imageData for where the decoder should begin
     *               parsing.
     * @param length the number of bytes, beginning at offset, to parse
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return The decoded NinePatchDrawable, or null if the image data could not be
     *         decoded, or, if opts is non-null, if opts requested only the
     *         size be returned (in opts.outWidth and opts.outHeight)
     */
    public static NinePatchDrawable decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length, opts);
        return createNinePatchDrawable(null, bitmap);
    }
    /**
     * Decode an immutable NinePatchDrawable from the specified byte array.
     *
     * @param data byte array of compressed image data
     * @param offset offset into imageData for where the decoder should begin
     *               parsing.
     * @param length the number of bytes, beginning at offset, to parse
     * @return The decoded NinePatchDrawable, or null if the image could not be decode.
     */
    public static NinePatchDrawable decodeByteArray(byte[] data, int offset, int length) {
        return decodeByteArray(data, offset, length, null);
    }
    /**
     * Decode an input stream into a NinePatchDrawable. If the input stream is null, or
     * cannot be used to decode a NinePatchDrawable, the function returns null.
     * The stream's position will be where ever it was after the encoded data
     * was read.
     *
     * @param is The input stream that holds the raw data to be decoded into a
     *           NinePatchDrawable.
     * @param outPadding If not null, return the padding rect for the NinePatchDrawable if
     *                   it exists, otherwise set padding to [-1,-1,-1,-1]. If
     *                   no NinePatchDrawable is returned (null) then padding is
     *                   unchanged.
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return The decoded NinePatchDrawable, or null if the image data could not be
     *         decoded, or, if opts is non-null, if opts requested only the
     *         size be returned (in opts.outWidth and opts.outHeight)
     */
    public static NinePatchDrawable decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
        return createNinePatchDrawable(null, bitmap);
    }
    public static NinePatchDrawable createNinePatchDrawable(Resources res, Bitmap bitmap){
        NinePatchDrawable drawable = null;
        byte[] chunk = bitmap.getNinePatchChunk();
        if(NinePatch.isNinePatchChunk(chunk)){
            Rect padding = readPadding(chunk);
            drawable = new NinePatchDrawable(res, bitmap, chunk, padding, null);
        }
        return drawable;
    }
    public static NinePatchDrawable createNinePatchDrawable(Bitmap bitmap){
        return createNinePatchDrawable(null, bitmap);
    }
    public static Rect readPadding(byte[] chunk){
        Rect rect = new Rect();
        if(NinePatch.isNinePatchChunk(chunk)){
            rect.left   = getInt(chunk, 12);
            rect.right  = getInt(chunk, 16);
            rect.top    = getInt(chunk, 20);
            rect.bottom = getInt(chunk, 24);
        }
        return rect;
    }
    public static int getInt(byte[] chunk, int from) {
        int b1 = chunk[from + 0];
        int b2 = chunk[from + 1];
        int b3 = chunk[from + 2];
        int b4 = chunk[from + 3];
        int i = b1<<0 | b2 <<8 | b3 << 16 | b4 << 24;
        return i;
    }
    /**
     * Decode an input stream into a NinePatchDrawable. If the input stream is null, or
     * cannot be used to decode a NinePatchDrawable, the function returns null.
     * The stream's position will be where ever it was after the encoded data
     * was read.
     *
     * @param is The input stream that holds the raw data to be decoded into a
     *           NinePatchDrawable.
     * @return The decoded NinePatchDrawable, or null if the image data could not be decoded.
     */
    public static NinePatchDrawable decodeStream(InputStream is) {
        return decodeStream(is, null, null);
    }
    /**
     * Decode a NinePatchDrawable from the file descriptor. If the NinePatchDrawable cannot be decoded
     * return null. The position within the descriptor will not be changed when
     * this returns, so the descriptor can be used again as-is.
     *
     * @param fd The file descriptor containing the NinePatchDrawable data to decode
     * @param outPadding If not null, return the padding rect for the NinePatchDrawable if
     *                   it exists, otherwise set padding to [-1,-1,-1,-1]. If
     *                   no NinePatchDrawable is returned (null) then padding is
     *                   unchanged.
     * @param opts null-ok; Options that control downsampling and whether the
     *             image should be completely decoded, or just is size returned.
     * @return the decoded NinePatchDrawable, or null
     */
    public static NinePatchDrawable decodeFileDescriptor(FileDescriptor fd, Rect outPadding,
                                                         BitmapFactory.Options opts) {
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, outPadding, opts);
        return createNinePatchDrawable(null, bitmap);
    }
    /**
     * Decode a NinePatchDrawable from the file descriptor. If the NinePatchDrawable cannot be decoded
     * return null. The position within the descriptor will not be changed when
     * this returns, so the descriptor can be used again as is.
     *
     * @param fd The file descriptor containing the NinePatchDrawable data to decode
     * @return the decoded NinePatchDrawable, or null
     */
    public static NinePatchDrawable decodeFileDescriptor(FileDescriptor fd) {
        return decodeFileDescriptor(fd, null, null);
    }
}
