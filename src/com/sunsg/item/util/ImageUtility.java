/**
 * 
 */
package com.sunsg.item.util;

import static android.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
import static android.media.ExifInterface.ORIENTATION_FLIP_VERTICAL;
import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.ORIENTATION_TRANSPOSE;
import static android.media.ExifInterface.ORIENTATION_TRANSVERSE;
import static android.media.ExifInterface.TAG_ORIENTATION;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;

import com.sina.weibo.sdk.utils.Utility;
import com.sunsg.item.util.Logger;

/**
 * 图片公共类
 * 
 * @ClassName: ImageUtility
 * @author WangJun
 * @date 2012-5-5 下午06:15:00
 */
public class ImageUtility {

	public final static int T_HEIGTH = 250;
	public final static int T_WIDTH = 250;
	public final static int OPTIONS = 2;
	public final static int FIELDS_WIDTH = 0;
	public final static int FIELDS_HEIGTH = 1;
	public static final int THUMB_SIZE = 90;

	/**
	 * 制作缩略图
	 * 
	 * @author WangJun
	 * @since 2012-6-11
	 * @param bitmap
	 * @return
	 */
	public final static Bitmap makeThumbnail(Bitmap bitmap) {
		return ThumbnailUtils.extractThumbnail(bitmap, T_WIDTH, T_HEIGTH,
				OPTIONS);
	}


	public final static Bitmap getThumbnailFor960(Bitmap bitmap) {
		Bitmap cropBimap = cropImage(960, 960, 0, bitmap);
		bitmap.recycle();
		bitmap = null;
		System.gc();
		return cropBimap;
	}

	/**
	 * 按指定要求裁剪图片
	 * 
	 * @author WangJun
	 * @since 2012-8-13
	 * @param heigth
	 * @param width
	 * @param scaleType
	 * @param bitmap
	 * @return
	 */
	public final static Bitmap cropImage(int heigth, int width, int scaleType,
			Bitmap bitmap) {
		int srcHeigth = bitmap.getHeight();
		int srcWidth = bitmap.getWidth();
		Bitmap outBitmap = null;
		if (srcHeigth > heigth && srcWidth > width) {
			Logger.e("crop image");
			int topMargin = (srcHeigth - heigth) / 2;
			int leftMargin = (srcWidth - width) / 2;
			outBitmap = Bitmap.createBitmap(bitmap, leftMargin, topMargin,
					width, heigth);
			if (bitmap.isRecycled()) {
				bitmap.recycle();
			}
		} else {
			boolean crop = false;
			int topMargin = 0;
			int leftMargin = 0;
			if (srcHeigth > heigth) {
				topMargin = (srcHeigth - heigth) / 2;
				crop = true;
			} else {
				heigth = bitmap.getHeight();
			}
			if (srcWidth > width) {
				leftMargin = (srcWidth - width) / 2;
				crop = true;
			} else {
				width = bitmap.getWidth();
			}
			if (crop) {
				outBitmap = Bitmap.createBitmap(bitmap, leftMargin, topMargin,
						leftMargin + width, topMargin + heigth);
				if (bitmap.isRecycled()) {
					bitmap.recycle();
				}
			} else {
				outBitmap = bitmap;
			}
		}
		return outBitmap;
	}

	/**
	 * 获取照片的方向
	 * 
	 * @author WangJun
	 * @since 2012-6-11
	 * @param path
	 * @return
	 */
	public final static int getPhotoOrientation(String path) {
		int orientation = ExifInterface.ORIENTATION_NORMAL;
		int degree = 0;
		try {
			ExifInterface exif = new ExifInterface(path);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		default:
			degree = 0;
			break;
		}
		return degree;
	}
	
	/**
	 * 获取照片的方向
	 * 
	 * @author GaoWen
	 * @since 2013-10-11
	 * @param orientation 方向
	 * @return
	 */
	public final static int getPhotoOrientation(int orientation) {
		int degree = 0;
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		default:
			degree = 0;
			break;
		}
		return degree;
	}
	
	

	/**
	 * 获取Image的宽高信息
	 * 
	 * @author WangJun
	 * @since 2012-8-13
	 * @param path
	 * @return
	 */
	public final static int[] getImageWidthAndHeigth(String path) {
		int[] result = new int[2];
		try {
			ExifInterface exif = new ExifInterface(path);
			result[FIELDS_WIDTH] = exif.getAttributeInt(
					ExifInterface.TAG_IMAGE_WIDTH, 0);
			result[FIELDS_HEIGTH] = exif.getAttributeInt(
					ExifInterface.TAG_IMAGE_LENGTH, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获得图片的exifInterface信息
	 * @param path
	 * @return
	 * @throws IOException
	 */
	static int getFileExifRotation(String path) {
		try {
			 ExifInterface exifInterface = new ExifInterface(path);
			    return exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL);
		} catch (Exception e) {
		}
		return 0;
	   
	  }

	/**
	 * 获取适当的缩放级别
	 * 
	 * @author WangJun
	 * @since 2012-8-13
	 * @param oHeigth
	 * @param oWidth
	 * @param tHeigth
	 * @param tWidth
	 * @return
	 */
	public final static int getThumbnailLevel(int oHeigth, int oWidth,
			int tHeigth, int tWidth) {
		int hLevel = oHeigth / tHeigth;
		int wLevel = oWidth / tWidth;
		int resultLevel = Math.max(hLevel, wLevel);
		if (resultLevel < 1) {
			resultLevel = 1;
		}
		return resultLevel;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
//		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), Config.ARGB_8888);
//		Canvas canvas = new Canvas(output);
//
//		final int color = 0xff424242;
//		final Paint paint = new Paint();
//		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//		final RectF rectF = new RectF(rect);
//		final float roundPx = bitmap.getWidth() / 2;
//
//		paint.setAntiAlias(true);
//		canvas.drawARGB(0, 0, 0, 0);
//		paint.setColor(color);
//		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//		canvas.drawBitmap(bitmap, rect, rect, paint);
//		return output;
		return getRoundeBitmap(bitmap);
	}
	
	
	public static Bitmap getRoundeBitmap(Bitmap bitmap){
		if(bitmap == null){
			return null;
		}
		 int width = bitmap.getWidth();
         int height = bitmap.getHeight();
         float roundPx;
         float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
         if (width <= height) {
                 roundPx = width / 2;
                 top = 0;
                 bottom = width;
                 left = 0;
                 right = width;
                 height = width;
                 dst_left = 0;
                 dst_top = 0;
                 dst_right = width;
                 dst_bottom = width;
         } else {
                 roundPx = height / 2;
                 float clip = (width - height) / 2;
                 left = clip;
                 right = width - clip;
                 top = 0;
                 bottom = height;
                 width = height;
                 dst_left = 0;
                 dst_top = 0;
                 dst_right = height;
                 dst_bottom = height;
         }
          
         Bitmap output = Bitmap.createBitmap(width,
                         height, Config.ARGB_8888);
         Canvas canvas = new Canvas(output);
          
         final int color = 0xff424242;
         final Paint paint = new Paint();
         final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
         final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
         final RectF rectF = new RectF(dst);

         paint.setAntiAlias(true);
          
         canvas.drawARGB(0, 0, 0, 0);
         paint.setColor(color);
         canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

         paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
         canvas.drawBitmap(bitmap, src, dst, paint);
         return output;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
	
//	public static Bitmap getBlurBitmap(Context context,Bitmap bitmap){
//		if(!Utility.isTeShuXingHao()){
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 2;
//			bitmap = Blur.fastblur(context, bitmap, 12);
//		}
//		return bitmap;
//	}
	
	public static Bitmap getBitmapFromeInputStream(FileInputStream is,Options opt){
		Bitmap bitmap = null;
		try {
			if(opt == null){
				opt = new Options();
//		        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		        opt.inPurgeable = true;  
		        opt.inInputShareable = true;
			}
			if(is != null) {
				byte[] bytes = new byte[1024];
				ByteArrayBuffer byteBuffer = new ByteArrayBuffer(1024);
				int len = -1;
				try {
					len = is.read(bytes);
					while (len > 0) {
						byteBuffer.append(bytes, 0, len);
						len = is.read(bytes);
					}
					is.close();
					byte[] bytess = byteBuffer.buffer();
					bitmap = BitmapFactory.decodeByteArray(bytess, 0,bytess.length, opt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			
		}
		
		return bitmap;
	}
	
	public static Bitmap getBitmapFromeFile(File file,Options opt){
		try {
			return getBitmapFromeInputStream(new FileInputStream(file),opt);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	static int getExifRotation(int orientation) {
	    int rotation;
	    switch (orientation) {
	      case ORIENTATION_ROTATE_90:
	      case ORIENTATION_TRANSPOSE:
	        rotation = 90;
	        break;
	      case ORIENTATION_ROTATE_180:
	      case ORIENTATION_FLIP_VERTICAL:
	        rotation = 180;
	        break;
	      case ORIENTATION_ROTATE_270:
	      case ORIENTATION_TRANSVERSE:
	        rotation = 270;
	        break;
	      default:
	        rotation = 0;
	    }
	    return rotation;
	  }
	
	static int getExifTranslation(int orientation)  {
	    int translation;
	    switch (orientation) {
	      case ORIENTATION_FLIP_HORIZONTAL:
	      case ORIENTATION_FLIP_VERTICAL:
	      case ORIENTATION_TRANSPOSE:
	      case ORIENTATION_TRANSVERSE:
	        translation = -1;
	        break;
	      default:
	        translation = 1;
	    }
	    return translation;
	  }
	
	 static byte[] toByteArray(InputStream input) throws IOException {
		    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024 * 4];
		    int n;
		    while (-1 != (n = input.read(buffer))) {
		      byteArrayOutputStream.write(buffer, 0, n);
		    }
		    return byteArrayOutputStream.toByteArray();
		  }
}
