/* The following code was generated by JFlex 1.4.1 on 22-9-23 下午3:01 */

package io.github.nickid2018.mcde.asmdl.highlight;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1
 * on 22-9-23 下午3:01 from the specification file
 * <tt>D:/MCDynamicExchanger/src/main/java/io/github/nickid2018/mcde/asmdl/highlight/ASMDLTokenMaker.flex</tt>
 */
public class ASMDLTokenMaker extends AbstractJFlexCTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\5\1\46\1\0\1\5\23\0\1\5\1\0\1\0\4\0"+
    "\1\0\2\7\2\0\1\0\1\0\1\6\1\4\1\42\1\40\1\41"+
    "\1\45\1\45\1\45\4\2\1\0\1\4\5\0\32\1\1\7\1\0"+
    "\1\7\1\0\1\3\1\0\1\12\1\24\1\10\1\21\1\15\1\22"+
    "\1\30\1\17\1\23\1\43\1\35\1\11\1\14\1\31\1\20\1\32"+
    "\1\37\1\25\1\13\1\16\1\26\1\27\1\36\1\44\1\33\1\34"+
    "\1\7\1\0\1\7\uff82\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\2\1\1\2\1\3\1\4\23\1\1\5\1\0"+
    "\107\1\1\6\15\1\1\7\5\1\1\7\32\1\1\7"+
    "\14\1\1\10\5\1\1\7\7\1\1\7\2\1\1\7"+
    "\4\1\1\10\31\1\1\11\7\1\1\7\250\1";

  private static int [] zzUnpackAction() {
    int [] result = new int[383];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\47\0\116\0\165\0\234\0\47\0\303\0\352"+
    "\0\u0111\0\u0138\0\u015f\0\u0186\0\u01ad\0\u01d4\0\u01fb\0\u0222"+
    "\0\u0249\0\u0270\0\u0297\0\u02be\0\u02e5\0\u030c\0\u0333\0\u035a"+
    "\0\u0381\0\47\0\u03a8\0\u03cf\0\u03f6\0\u041d\0\u0444\0\u046b"+
    "\0\u0492\0\u04b9\0\u04e0\0\u0507\0\u052e\0\u0555\0\u057c\0\u05a3"+
    "\0\u05ca\0\u05f1\0\u0618\0\u063f\0\u0666\0\u068d\0\u06b4\0\u06db"+
    "\0\u0702\0\u0729\0\u0750\0\u0777\0\u079e\0\u07c5\0\u07ec\0\u0813"+
    "\0\u083a\0\u0861\0\u0888\0\u08af\0\u08d6\0\u08fd\0\u0924\0\u094b"+
    "\0\u0972\0\u0999\0\u09c0\0\u09e7\0\u0a0e\0\u0a35\0\u0a5c\0\u0a83"+
    "\0\u0aaa\0\u0ad1\0\u0af8\0\u0b1f\0\u0b46\0\u0b6d\0\u0b94\0\u0bbb"+
    "\0\u0be2\0\u0c09\0\u0c30\0\u0c57\0\u0c7e\0\u0ca5\0\u0ccc\0\u0cf3"+
    "\0\u0d1a\0\u0d41\0\u0d68\0\u0d8f\0\u0db6\0\u0ddd\0\u0e04\0\u0e2b"+
    "\0\u0e52\0\u0e79\0\u03a8\0\u0ea0\0\u0ec7\0\u0eee\0\u0f15\0\u0f3c"+
    "\0\u0f63\0\u0f8a\0\u0fb1\0\u0fd8\0\u0fff\0\u1026\0\u104d\0\u1074"+
    "\0\u109b\0\u10c2\0\u10e9\0\u1110\0\u1137\0\u115e\0\116\0\u1185"+
    "\0\u11ac\0\u11d3\0\u11fa\0\u1221\0\u1248\0\u126f\0\u1296\0\u12bd"+
    "\0\u12e4\0\u130b\0\u1332\0\u1359\0\u1380\0\u13a7\0\u13ce\0\u13f5"+
    "\0\u141c\0\u1443\0\u146a\0\u1491\0\u14b8\0\u14df\0\u1506\0\u152d"+
    "\0\u1554\0\u157b\0\u15a2\0\u15c9\0\u15f0\0\u1617\0\u163e\0\u1665"+
    "\0\u168c\0\u16b3\0\u16da\0\u1701\0\u1728\0\u174f\0\u1776\0\u179d"+
    "\0\u17c4\0\u17eb\0\u1812\0\u1839\0\u1860\0\u1887\0\u18ae\0\u18d5"+
    "\0\u18fc\0\u1923\0\u194a\0\u1971\0\u1998\0\u19bf\0\u19e6\0\u1a0d"+
    "\0\u1a34\0\u1a5b\0\u1a82\0\u1aa9\0\116\0\u1ad0\0\u1af7\0\u1b1e"+
    "\0\u1b45\0\u1b6c\0\u1b93\0\u1bba\0\u1860\0\u1be1\0\u1c08\0\u1c2f"+
    "\0\u1c56\0\u1998\0\u1c7d\0\u1ca4\0\u1ccb\0\u1cf2\0\u1d19\0\u1d40"+
    "\0\u1d67\0\u1d8e\0\u1db5\0\u1ddc\0\u1e03\0\u1e2a\0\116\0\u1e51"+
    "\0\u1e78\0\u1e9f\0\u1ec6\0\u1eed\0\u1f14\0\u1f3b\0\u1f62\0\u1f89"+
    "\0\u1fb0\0\u1fd7\0\u1ffe\0\u2025\0\u204c\0\u2073\0\u209a\0\u20c1"+
    "\0\u20e8\0\u210f\0\u2136\0\u215d\0\u2184\0\u21ab\0\u21d2\0\u21f9"+
    "\0\u2220\0\u2247\0\u226e\0\u2295\0\u22bc\0\u22e3\0\u230a\0\u2331"+
    "\0\u2358\0\u237f\0\u23a6\0\u23cd\0\u23f4\0\u241b\0\u2442\0\u2469"+
    "\0\u2490\0\u24b7\0\u24de\0\u2505\0\u252c\0\u2553\0\u257a\0\u25a1"+
    "\0\u25c8\0\u25ef\0\u2616\0\u263d\0\u2664\0\u268b\0\u26b2\0\u26d9"+
    "\0\u2700\0\u2727\0\u274e\0\u2775\0\u279c\0\u27c3\0\u27ea\0\u2811"+
    "\0\u2838\0\u285f\0\u2886\0\u28ad\0\u28d4\0\u28fb\0\u2922\0\u2949"+
    "\0\u2970\0\u2997\0\u29be\0\u29e5\0\u2a0c\0\u2a33\0\u2a5a\0\u2a81"+
    "\0\u2aa8\0\u2acf\0\u2af6\0\u2b1d\0\u2b44\0\u2b6b\0\u2b92\0\u2bb9"+
    "\0\u2be0\0\u2c07\0\u2c2e\0\u2c55\0\u2c7c\0\u2ca3\0\u2cca\0\u2cf1"+
    "\0\u2d18\0\u2d3f\0\u2d66\0\u2d8d\0\u2db4\0\u2ddb\0\u2e02\0\u2e29"+
    "\0\u2e50\0\u2e77\0\u2e9e\0\u2ec5\0\u2eec\0\u2f13\0\u2f3a\0\u2f61"+
    "\0\u2f88\0\u2faf\0\u2fd6\0\u2ffd\0\u3024\0\u304b\0\u3072\0\u3099"+
    "\0\u30c0\0\u30e7\0\u310e\0\u3135\0\u315c\0\u3183\0\u31aa\0\u31d1"+
    "\0\u31f8\0\u321f\0\u3246\0\u326d\0\u3294\0\u32bb\0\u32e2\0\u3309"+
    "\0\u3330\0\u3357\0\u337e\0\u33a5\0\u33cc\0\u33f3\0\u341a\0\u3441"+
    "\0\u3468\0\u348f\0\u34b6\0\u34dd\0\u3504\0\u352b\0\u3552\0\u3579"+
    "\0\u35a0\0\u35c7\0\u35ee\0\u3615\0\u363c\0\u3663\0\u368a\0\u36b1"+
    "\0\u36d8\0\u36ff\0\u3726\0\u374d\0\u3774\0\u379b\0\u37c2\0\u37e9"+
    "\0\u3810\0\u3837\0\u385e\0\u3885\0\u38ac\0\u38d3\0\u38fa";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[383];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\3\1\2\1\5\1\2\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\3\1\25"+
    "\1\26\1\27\1\30\5\3\3\4\1\31\1\3\1\4"+
    "\1\32\50\0\4\3\3\0\36\3\3\0\1\4\3\0"+
    "\1\33\31\0\3\4\2\0\1\4\6\0\1\5\42\0"+
    "\4\3\3\0\1\3\1\34\1\35\4\3\1\36\1\37"+
    "\25\3\2\0\4\3\3\0\1\40\1\41\1\42\1\43"+
    "\1\44\3\3\1\45\1\46\1\3\1\47\1\3\1\50"+
    "\1\51\2\3\1\52\7\3\1\53\4\3\2\0\4\3"+
    "\3\0\1\54\1\41\1\35\1\55\2\3\1\56\5\3"+
    "\1\57\1\60\3\3\1\61\14\3\2\0\4\3\3\0"+
    "\2\3\1\35\3\3\1\62\1\63\3\3\1\64\2\3"+
    "\1\65\4\3\1\66\2\3\1\67\7\3\2\0\4\3"+
    "\3\0\5\3\1\70\2\3\1\71\5\3\1\72\17\3"+
    "\2\0\4\3\3\0\21\3\1\73\14\3\2\0\4\3"+
    "\3\0\2\3\1\74\12\3\1\75\20\3\2\0\4\3"+
    "\3\0\2\3\1\76\33\3\2\0\4\3\3\0\16\3"+
    "\1\77\17\3\2\0\4\3\3\0\1\100\1\41\1\101"+
    "\1\102\1\44\3\3\1\103\1\104\3\3\1\50\1\105"+
    "\2\3\1\52\7\3\1\106\4\3\2\0\4\3\3\0"+
    "\1\107\1\110\1\111\1\102\1\44\4\3\1\104\1\3"+
    "\1\112\1\3\1\50\3\3\1\52\7\3\1\113\4\3"+
    "\2\0\4\3\3\0\1\114\1\41\1\101\1\43\1\44"+
    "\4\3\1\104\1\115\1\116\1\3\1\50\1\51\2\3"+
    "\1\117\7\3\1\120\4\3\2\0\4\3\3\0\2\3"+
    "\1\35\5\3\1\121\2\3\1\122\1\3\1\123\5\3"+
    "\1\124\12\3\2\0\4\3\3\0\5\3\1\125\30\3"+
    "\2\0\4\3\3\0\2\3\1\126\5\3\1\127\25\3"+
    "\2\0\4\3\3\0\5\3\1\130\2\3\1\131\25\3"+
    "\2\0\4\3\3\0\2\3\1\132\2\3\1\133\2\3"+
    "\1\134\25\3\2\0\4\3\3\0\2\3\1\135\2\3"+
    "\1\136\2\3\1\137\4\3\1\140\1\141\17\3\2\0"+
    "\4\3\3\0\3\3\1\142\32\3\3\0\1\143\35\0"+
    "\3\143\2\0\1\143\2\0\4\3\3\0\2\3\1\144"+
    "\33\3\2\0\4\3\3\0\1\3\1\41\1\3\1\55"+
    "\32\3\2\0\4\3\3\0\2\3\1\145\2\3\1\146"+
    "\30\3\2\0\4\3\3\0\21\3\1\147\14\3\2\0"+
    "\4\3\3\0\4\3\1\134\3\3\1\150\25\3\2\0"+
    "\4\3\3\0\10\3\1\151\25\3\2\0\4\3\3\0"+
    "\1\3\1\41\1\3\1\55\5\3\1\152\2\3\1\153"+
    "\21\3\2\0\4\3\3\0\6\3\1\154\1\155\6\3"+
    "\1\156\17\3\2\0\4\3\3\0\16\3\1\157\17\3"+
    "\2\0\4\3\3\0\10\3\1\160\25\3\2\0\4\3"+
    "\3\0\1\161\12\3\1\162\22\3\2\0\4\3\3\0"+
    "\21\3\1\163\14\3\2\0\4\3\3\0\5\3\1\164"+
    "\30\3\2\0\4\3\3\0\3\3\1\165\32\3\2\0"+
    "\4\3\3\0\5\3\1\166\30\3\2\0\4\3\3\0"+
    "\11\3\3\167\22\3\2\0\4\3\3\0\10\3\1\170"+
    "\25\3\2\0\4\3\3\0\6\3\1\154\27\3\2\0"+
    "\4\3\3\0\7\3\1\171\26\3\2\0\4\3\3\0"+
    "\3\3\1\172\32\3\2\0\4\3\3\0\5\3\1\173"+
    "\7\3\1\174\20\3\2\0\4\3\3\0\5\3\1\175"+
    "\13\3\1\176\14\3\2\0\4\3\3\0\2\3\1\177"+
    "\12\3\1\200\20\3\2\0\4\3\3\0\10\3\1\201"+
    "\25\3\2\0\4\3\3\0\20\3\1\202\1\3\1\203"+
    "\13\3\2\0\4\3\3\0\22\3\1\204\13\3\2\0"+
    "\4\3\3\0\21\3\1\205\14\3\2\0\4\3\3\0"+
    "\2\3\1\134\33\3\2\0\4\3\3\0\6\3\1\206"+
    "\27\3\2\0\4\3\3\0\21\3\1\207\14\3\2\0"+
    "\4\3\3\0\1\3\1\210\34\3\2\0\4\3\3\0"+
    "\16\3\1\211\17\3\2\0\4\3\3\0\14\3\1\212"+
    "\21\3\2\0\4\3\3\0\2\3\1\213\13\3\1\214"+
    "\4\3\1\215\12\3\2\0\4\3\3\0\21\3\1\216"+
    "\14\3\2\0\4\3\3\0\6\3\1\217\27\3\2\0"+
    "\4\3\3\0\4\3\1\220\3\3\1\150\25\3\2\0"+
    "\4\3\3\0\1\3\1\41\1\3\1\55\5\3\1\152"+
    "\24\3\2\0\4\3\3\0\6\3\1\154\7\3\1\156"+
    "\17\3\2\0\4\3\3\0\16\3\1\221\17\3\2\0"+
    "\4\3\3\0\13\3\1\162\22\3\2\0\4\3\3\0"+
    "\22\3\1\222\13\3\2\0\4\3\3\0\1\3\1\167"+
    "\10\3\2\167\22\3\2\0\4\3\3\0\4\3\1\220"+
    "\3\3\1\223\25\3\2\0\4\3\3\0\10\3\1\224"+
    "\25\3\2\0\4\3\3\0\1\3\1\225\1\3\1\55"+
    "\5\3\1\152\24\3\2\0\4\3\3\0\5\3\1\226"+
    "\13\3\1\227\14\3\2\0\4\3\3\0\1\3\1\167"+
    "\7\3\1\167\1\3\1\167\22\3\2\0\4\3\3\0"+
    "\10\3\1\230\25\3\2\0\2\3\1\231\1\3\3\0"+
    "\1\3\1\232\3\3\1\233\12\3\1\232\1\234\14\3"+
    "\2\0\4\3\3\0\21\3\1\235\14\3\2\0\4\3"+
    "\3\0\3\3\1\236\1\3\1\166\1\237\10\3\1\240"+
    "\1\3\1\217\14\3\2\0\4\3\3\0\2\167\1\3"+
    "\1\167\5\3\2\167\1\3\1\167\21\3\2\0\4\3"+
    "\3\0\10\3\1\241\25\3\2\0\4\3\3\0\22\3"+
    "\1\203\13\3\2\0\4\3\3\0\13\3\1\242\22\3"+
    "\2\0\4\3\3\0\6\3\1\243\27\3\2\0\4\3"+
    "\3\0\1\244\5\3\1\245\27\3\2\0\4\3\3\0"+
    "\1\3\1\246\13\3\1\247\20\3\2\0\4\3\3\0"+
    "\1\3\1\250\34\3\2\0\4\3\3\0\6\3\1\251"+
    "\27\3\2\0\4\3\3\0\6\3\1\252\27\3\2\0"+
    "\4\3\3\0\6\3\1\253\27\3\2\0\4\3\3\0"+
    "\3\3\1\254\22\3\1\255\7\3\2\0\4\3\3\0"+
    "\22\3\1\167\13\3\2\0\4\3\3\0\15\3\1\256"+
    "\20\3\2\0\4\3\3\0\15\3\1\257\20\3\2\0"+
    "\4\3\3\0\22\3\1\260\13\3\2\0\4\3\3\0"+
    "\10\3\1\261\2\3\1\262\22\3\2\0\4\3\3\0"+
    "\6\3\1\251\5\3\1\263\21\3\2\0\4\3\3\0"+
    "\15\3\1\167\20\3\2\0\4\3\3\0\3\3\1\264"+
    "\32\3\2\0\4\3\3\0\15\3\1\265\20\3\2\0"+
    "\4\3\3\0\1\266\35\3\2\0\4\3\3\0\3\3"+
    "\1\267\32\3\2\0\4\3\3\0\21\3\1\270\14\3"+
    "\2\0\4\3\3\0\2\3\1\152\33\3\2\0\4\3"+
    "\3\0\11\3\1\167\24\3\2\0\4\3\3\0\5\3"+
    "\1\271\30\3\2\0\4\3\3\0\10\3\1\272\25\3"+
    "\2\0\4\3\3\0\1\3\1\167\13\3\1\167\20\3"+
    "\2\0\4\3\3\0\14\3\1\167\21\3\2\0\4\3"+
    "\3\0\1\3\1\167\34\3\2\0\4\3\3\0\25\3"+
    "\1\273\10\3\2\0\2\3\1\274\1\3\3\0\36\3"+
    "\2\0\4\3\3\0\17\3\1\167\16\3\2\0\4\3"+
    "\3\0\5\3\1\167\30\3\2\0\4\3\3\0\4\3"+
    "\1\167\1\3\1\275\27\3\2\0\4\3\3\0\7\3"+
    "\1\142\26\3\2\0\4\3\3\0\20\3\1\167\15\3"+
    "\2\0\4\3\3\0\21\3\1\276\14\3\2\0\4\3"+
    "\3\0\15\3\1\277\20\3\2\0\4\3\3\0\6\3"+
    "\1\300\27\3\2\0\4\3\3\0\6\3\1\275\27\3"+
    "\2\0\4\3\3\0\2\3\1\301\33\3\2\0\4\3"+
    "\3\0\26\3\1\302\7\3\2\0\4\3\3\0\10\3"+
    "\1\303\25\3\2\0\4\3\3\0\6\3\1\304\27\3"+
    "\2\0\4\3\3\0\13\3\1\305\22\3\2\0\4\3"+
    "\3\0\15\3\1\306\20\3\2\0\4\3\3\0\21\3"+
    "\1\307\14\3\2\0\4\3\3\0\16\3\1\310\17\3"+
    "\2\0\4\3\3\0\5\3\1\311\30\3\2\0\4\3"+
    "\3\0\1\312\5\3\1\313\27\3\2\0\4\3\3\0"+
    "\7\3\1\314\26\3\2\0\4\3\3\0\13\3\1\315"+
    "\22\3\2\0\4\3\3\0\6\3\1\316\27\3\2\0"+
    "\4\3\3\0\4\3\1\317\31\3\2\0\4\3\3\0"+
    "\1\3\1\320\34\3\2\0\4\3\3\0\21\3\1\321"+
    "\14\3\2\0\4\3\3\0\5\3\1\317\30\3\2\0"+
    "\2\3\1\322\1\3\3\0\36\3\2\0\4\3\3\0"+
    "\11\3\1\323\24\3\2\0\4\3\3\0\5\3\1\324"+
    "\30\3\2\0\4\3\3\0\22\3\1\325\13\3\2\0"+
    "\4\3\3\0\14\3\1\323\21\3\2\0\2\3\1\326"+
    "\1\3\3\0\31\3\1\327\4\3\2\0\4\3\3\0"+
    "\21\3\1\330\14\3\2\0\4\3\3\0\2\3\1\331"+
    "\33\3\2\0\4\3\3\0\3\3\1\214\4\3\1\151"+
    "\25\3\2\0\4\3\3\0\1\3\1\332\34\3\2\0"+
    "\4\3\3\0\2\3\1\271\33\3\2\0\4\3\3\0"+
    "\21\3\1\333\14\3\2\0\4\3\3\0\2\3\1\334"+
    "\10\3\1\335\22\3\2\0\4\3\3\0\5\3\2\167"+
    "\27\3\2\0\4\3\3\0\27\3\1\167\6\3\2\0"+
    "\4\3\3\0\5\3\1\167\2\3\1\336\5\3\1\337"+
    "\17\3\2\0\4\3\3\0\1\167\35\3\2\0\4\3"+
    "\3\0\6\3\1\340\27\3\2\0\4\3\3\0\5\3"+
    "\1\341\30\3\2\0\4\3\3\0\10\3\1\342\25\3"+
    "\2\0\4\3\3\0\1\3\1\343\34\3\2\0\4\3"+
    "\3\0\11\3\1\344\24\3\2\0\4\3\3\0\5\3"+
    "\1\265\30\3\2\0\4\3\3\0\10\3\1\345\25\3"+
    "\2\0\4\3\3\0\16\3\1\346\17\3\2\0\4\3"+
    "\3\0\16\3\1\214\17\3\2\0\4\3\3\0\2\3"+
    "\1\347\33\3\2\0\4\3\3\0\2\3\1\350\33\3"+
    "\2\0\4\3\3\0\3\3\1\351\6\3\1\352\23\3"+
    "\2\0\4\3\3\0\10\3\1\167\25\3\2\0\4\3"+
    "\3\0\13\3\1\353\22\3\2\0\4\3\3\0\6\3"+
    "\1\354\27\3\2\0\4\3\3\0\2\3\1\355\33\3"+
    "\2\0\4\3\3\0\2\3\1\356\33\3\2\0\4\3"+
    "\3\0\4\3\1\357\31\3\2\0\4\3\3\0\31\3"+
    "\1\167\4\3\2\0\4\3\3\0\6\3\1\360\27\3"+
    "\2\0\4\3\3\0\17\3\1\361\16\3\2\0\4\3"+
    "\3\0\1\3\1\304\34\3\2\0\4\3\3\0\3\3"+
    "\1\317\32\3\2\0\4\3\3\0\25\3\1\362\10\3"+
    "\2\0\4\3\3\0\6\3\1\363\27\3\2\0\4\3"+
    "\3\0\3\3\1\364\32\3\2\0\4\3\3\0\1\3"+
    "\1\317\34\3\2\0\4\3\3\0\15\3\1\163\20\3"+
    "\2\0\4\3\3\0\16\3\1\365\17\3\2\0\4\3"+
    "\3\0\11\3\1\366\24\3\2\0\4\3\3\0\3\3"+
    "\1\367\32\3\2\0\4\3\3\0\10\3\1\370\25\3"+
    "\2\0\4\3\3\0\15\3\1\371\20\3\2\0\4\3"+
    "\3\0\23\3\1\372\12\3\2\0\4\3\3\0\6\3"+
    "\1\373\27\3\2\0\4\3\3\0\13\3\1\374\22\3"+
    "\2\0\4\3\3\0\1\375\35\3\2\0\4\3\3\0"+
    "\6\3\1\265\27\3\2\0\4\3\3\0\2\3\1\376"+
    "\33\3\2\0\4\3\3\0\3\3\1\377\32\3\2\0"+
    "\4\3\3\0\15\3\1\317\20\3\2\0\4\3\3\0"+
    "\7\3\1\u0100\26\3\2\0\4\3\3\0\7\3\1\u0101"+
    "\26\3\2\0\4\3\3\0\10\3\1\332\25\3\2\0"+
    "\4\3\3\0\6\3\1\u0102\27\3\2\0\4\3\3\0"+
    "\13\3\1\u0103\22\3\2\0\4\3\3\0\5\3\1\u0104"+
    "\30\3\2\0\4\3\3\0\3\3\1\u0105\32\3\2\0"+
    "\4\3\3\0\1\u0106\35\3\2\0\4\3\3\0\1\3"+
    "\1\243\34\3\2\0\4\3\3\0\15\3\1\u0107\20\3"+
    "\2\0\4\3\3\0\1\3\1\167\16\3\1\167\15\3"+
    "\2\0\4\3\3\0\34\3\1\u0108\1\3\2\0\2\3"+
    "\1\326\1\3\3\0\36\3\2\0\4\3\3\0\3\3"+
    "\1\u0109\32\3\2\0\4\3\3\0\6\3\1\265\2\3"+
    "\1\167\24\3\2\0\4\3\3\0\11\3\1\317\24\3"+
    "\2\0\4\3\3\0\3\3\1\u010a\32\3\2\0\4\3"+
    "\3\0\1\u010b\35\3\2\0\4\3\3\0\1\u010c\35\3"+
    "\2\0\4\3\3\0\21\3\1\u010d\14\3\2\0\4\3"+
    "\3\0\1\3\1\157\34\3\2\0\4\3\3\0\2\3"+
    "\1\u010e\33\3\2\0\4\3\3\0\15\3\1\u010f\20\3"+
    "\2\0\4\3\3\0\25\3\1\u0110\10\3\2\0\4\3"+
    "\3\0\5\3\1\u0111\30\3\2\0\4\3\3\0\20\3"+
    "\1\214\15\3\2\0\4\3\3\0\15\3\1\u0112\20\3"+
    "\2\0\4\3\3\0\15\3\1\u0113\20\3\2\0\4\3"+
    "\3\0\15\3\1\u0114\20\3\2\0\4\3\3\0\6\3"+
    "\1\u0115\27\3\2\0\4\3\3\0\6\3\1\u0116\27\3"+
    "\2\0\4\3\3\0\13\3\1\u0117\22\3\2\0\4\3"+
    "\3\0\17\3\1\214\16\3\2\0\4\3\3\0\4\3"+
    "\1\u0118\2\3\1\u0119\26\3\2\0\4\3\3\0\15\3"+
    "\1\u011a\20\3\2\0\4\3\3\0\4\3\1\u011b\31\3"+
    "\2\0\4\3\3\0\13\3\1\u011c\22\3\2\0\4\3"+
    "\3\0\5\3\1\u011d\30\3\2\0\4\3\3\0\2\3"+
    "\1\u011e\33\3\2\0\4\3\3\0\1\u011f\35\3\2\0"+
    "\4\3\3\0\2\3\1\u0120\33\3\2\0\4\3\3\0"+
    "\6\3\1\u0121\27\3\2\0\4\3\3\0\22\3\1\u0104"+
    "\13\3\2\0\4\3\3\0\23\3\1\u0122\12\3\2\0"+
    "\4\3\3\0\6\3\1\u0123\27\3\2\0\4\3\3\0"+
    "\26\3\1\167\7\3\2\0\4\3\3\0\2\3\1\305"+
    "\33\3\2\0\4\3\3\0\1\3\1\u0124\34\3\2\0"+
    "\4\3\3\0\2\3\1\u0125\33\3\2\0\4\3\3\0"+
    "\1\317\35\3\2\0\4\3\3\0\6\3\1\317\27\3"+
    "\2\0\4\3\3\0\6\3\1\u0126\27\3\2\0\4\3"+
    "\3\0\7\3\1\167\26\3\2\0\4\3\3\0\10\3"+
    "\1\u0127\25\3\2\0\4\3\3\0\5\3\1\177\30\3"+
    "\2\0\4\3\3\0\10\3\1\u0128\25\3\2\0\4\3"+
    "\3\0\2\3\1\u0129\33\3\2\0\4\3\3\0\3\3"+
    "\1\u012a\32\3\2\0\4\3\3\0\13\3\1\u012b\22\3"+
    "\2\0\4\3\3\0\2\3\1\u012c\33\3\2\0\4\3"+
    "\3\0\1\u012d\35\3\2\0\4\3\3\0\30\3\2\167"+
    "\4\3\2\0\4\3\3\0\6\3\1\u012e\27\3\2\0"+
    "\4\3\3\0\6\3\1\u012f\27\3\2\0\4\3\3\0"+
    "\4\3\1\u0130\31\3\2\0\4\3\3\0\4\3\1\u0131"+
    "\31\3\2\0\4\3\3\0\21\3\1\u0132\14\3\2\0"+
    "\4\3\3\0\21\3\1\u0133\14\3\2\0\4\3\3\0"+
    "\12\3\1\u0134\23\3\2\0\4\3\3\0\5\3\1\u0135"+
    "\30\3\2\0\4\3\3\0\2\3\1\u0136\33\3\2\0"+
    "\4\3\3\0\11\3\1\u0137\24\3\2\0\4\3\3\0"+
    "\21\3\1\167\14\3\2\0\4\3\3\0\20\3\1\264"+
    "\15\3\2\0\4\3\3\0\13\3\1\u0138\22\3\2\0"+
    "\4\3\3\0\2\3\1\u0139\33\3\2\0\4\3\3\0"+
    "\5\3\1\u013a\30\3\2\0\4\3\3\0\5\3\1\u013b"+
    "\30\3\2\0\4\3\3\0\10\3\1\u013c\25\3\2\0"+
    "\4\3\3\0\15\3\1\u013d\20\3\2\0\4\3\3\0"+
    "\5\3\1\u013e\30\3\2\0\4\3\3\0\6\3\1\u013f"+
    "\27\3\2\0\4\3\3\0\1\u0140\35\3\2\0\4\3"+
    "\3\0\6\3\1\214\27\3\2\0\4\3\3\0\2\3"+
    "\1\u013c\33\3\2\0\4\3\3\0\21\3\1\u0141\14\3"+
    "\2\0\2\3\1\u0142\1\3\3\0\36\3\2\0\4\3"+
    "\3\0\21\3\1\u0143\14\3\2\0\2\3\1\u010d\1\3"+
    "\3\0\36\3\2\0\4\3\3\0\5\3\1\u0144\30\3"+
    "\2\0\4\3\3\0\6\3\1\u0145\27\3\2\0\4\3"+
    "\3\0\16\3\1\u0146\17\3\2\0\4\3\3\0\15\3"+
    "\1\u0147\20\3\2\0\4\3\3\0\15\3\1\u0148\20\3"+
    "\2\0\4\3\3\0\21\3\1\u0149\14\3\2\0\4\3"+
    "\3\0\26\3\1\u014a\7\3\2\0\4\3\3\0\5\3"+
    "\1\u014b\30\3\2\0\4\3\3\0\6\3\1\u014c\27\3"+
    "\2\0\4\3\3\0\1\3\1\u014d\34\3\2\0\2\3"+
    "\1\u014e\1\3\3\0\36\3\2\0\2\3\1\u014f\1\3"+
    "\3\0\36\3\2\0\4\3\3\0\22\3\1\u0150\13\3"+
    "\2\0\4\3\3\0\22\3\1\u0151\13\3\2\0\4\3"+
    "\3\0\16\3\1\337\17\3\2\0\4\3\3\0\1\u0152"+
    "\35\3\2\0\4\3\3\0\2\3\1\u0153\33\3\2\0"+
    "\4\3\3\0\3\3\1\u0154\5\3\1\366\1\3\1\u0155"+
    "\3\3\1\u0156\16\3\2\0\4\3\3\0\21\3\1\265"+
    "\14\3\2\0\2\3\1\u0157\1\3\3\0\36\3\2\0"+
    "\4\3\3\0\1\3\1\214\34\3\2\0\4\3\3\0"+
    "\6\3\1\u0158\27\3\2\0\4\3\3\0\1\3\1\152"+
    "\34\3\2\0\4\3\3\0\4\3\1\u0159\31\3\2\0"+
    "\4\3\3\0\3\3\1\u0141\32\3\2\0\4\3\3\0"+
    "\2\3\1\u015a\33\3\2\0\4\3\3\0\6\3\1\u015b"+
    "\27\3\2\0\4\3\3\0\6\3\1\u015c\27\3\2\0"+
    "\4\3\3\0\6\3\1\u015d\27\3\2\0\4\3\3\0"+
    "\6\3\1\167\27\3\2\0\4\3\3\0\30\3\1\167"+
    "\1\3\1\167\3\3\2\0\4\3\3\0\2\3\1\u015e"+
    "\33\3\2\0\4\3\3\0\21\3\1\u015f\14\3\2\0"+
    "\4\3\3\0\13\3\1\u0160\22\3\2\0\4\3\3\0"+
    "\15\3\1\214\20\3\2\0\4\3\3\0\10\3\1\u0161"+
    "\25\3\2\0\4\3\3\0\5\3\1\u0162\30\3\2\0"+
    "\4\3\3\0\5\3\1\175\30\3\2\0\4\3\3\0"+
    "\13\3\1\u012c\22\3\2\0\4\3\3\0\21\3\1\375"+
    "\14\3\2\0\4\3\3\0\1\377\35\3\2\0\4\3"+
    "\3\0\2\3\1\u0163\33\3\2\0\4\3\3\0\30\3"+
    "\3\167\3\3\2\0\4\3\3\0\4\3\1\u0164\23\3"+
    "\3\167\2\3\1\167\2\0\4\3\3\0\5\3\1\233"+
    "\13\3\1\163\14\3\2\0\4\3\3\0\1\3\1\232"+
    "\3\3\1\233\12\3\1\232\1\163\14\3\2\0\4\3"+
    "\3\0\5\3\1\u0165\30\3\2\0\4\3\3\0\1\214"+
    "\35\3\2\0\4\3\3\0\6\3\1\u0116\13\3\1\u0166"+
    "\13\3\2\0\4\3\3\0\21\3\1\u0167\14\3\2\0"+
    "\4\3\3\0\13\3\1\u0168\22\3\2\0\4\3\3\0"+
    "\1\u0169\35\3\2\0\4\3\3\0\13\3\1\235\22\3"+
    "\2\0\4\3\3\0\14\3\1\u015b\21\3\2\0\4\3"+
    "\3\0\23\3\1\167\12\3\2\0\4\3\3\0\5\3"+
    "\1\142\30\3\2\0\4\3\3\0\5\3\1\152\30\3"+
    "\2\0\4\3\3\0\5\3\1\332\30\3\2\0\4\3"+
    "\3\0\4\3\1\u0158\31\3\2\0\4\3\3\0\20\3"+
    "\1\u016a\15\3\2\0\4\3\3\0\10\3\1\u016b\25\3"+
    "\2\0\4\3\3\0\21\3\1\u016c\14\3\2\0\4\3"+
    "\3\0\21\3\1\u013e\12\3\1\u016d\1\3\2\0\4\3"+
    "\3\0\3\3\1\u016e\32\3\2\0\4\3\3\0\30\3"+
    "\1\167\5\3\2\0\4\3\3\0\10\3\1\u016f\25\3"+
    "\2\0\4\3\3\0\5\3\1\u0170\30\3\2\0\4\3"+
    "\3\0\6\3\1\u0171\27\3\2\0\4\3\3\0\15\3"+
    "\1\u0172\20\3\2\0\4\3\3\0\10\3\1\u0173\25\3"+
    "\2\0\4\3\3\0\6\3\1\377\27\3\2\0\4\3"+
    "\3\0\21\3\1\317\14\3\2\0\4\3\3\0\13\3"+
    "\1\u0174\22\3\2\0\4\3\3\0\13\3\1\u0141\22\3"+
    "\2\0\4\3\3\0\3\3\1\167\32\3\2\0\4\3"+
    "\3\0\12\3\1\167\23\3\2\0\4\3\3\0\1\u0175"+
    "\35\3\2\0\4\3\3\0\5\3\1\u0176\30\3\2\0"+
    "\4\3\3\0\6\3\1\u0177\27\3\2\0\4\3\3\0"+
    "\4\3\1\u0178\31\3\2\0\4\3\3\0\24\3\1\u015d"+
    "\11\3\2\0\4\3\3\0\13\3\1\u0179\22\3\2\0"+
    "\4\3\3\0\15\3\1\u017a\20\3\2\0\4\3\3\0"+
    "\16\3\1\u0179\17\3\2\0\4\3\3\0\22\3\1\u017b"+
    "\13\3\2\0\4\3\3\0\2\3\1\157\33\3\2\0"+
    "\4\3\3\0\12\3\1\u017c\23\3\2\0\4\3\3\0"+
    "\10\3\1\u017d\25\3\2\0\4\3\3\0\2\3\1\u017e"+
    "\33\3\2\0\4\3\3\0\21\3\1\u017f\14\3\2\0"+
    "\4\3\3\0\1\163\35\3\2\0\4\3\3\0\5\3"+
    "\1\u0120\30\3\1\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[14625];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\3\1\1\11\23\1\1\11\1\0\u0164\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[383];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */
   public ASMDLTokenMaker() {
   }

/**
    * Adds the token specified to the current linked list of tokens.
    *
    * @param tokenType The token's type.
    * @see #addToken(int, int, int)
    */
   private void addHyperlinkToken(int start, int end, int tokenType) {
      int so = start + offsetShift;
      addToken(zzBuffer, start,end, tokenType, so, true);
   }

   /**
    * Adds the token specified to the current linked list of tokens.
    *
    * @param tokenType The token's type.
    */
   private void addToken(int tokenType) {
      addToken(zzStartRead, zzMarkedPos-1, tokenType);
   }

   /**
    * Adds the token specified to the current linked list of tokens.
    *
    * @param tokenType The token's type.
    * @see #addHyperlinkToken(int, int, int)
    */
   private void addToken(int start, int end, int tokenType) {
      int so = start + offsetShift;
      addToken(zzBuffer, start,end, tokenType, so, false);
   }

   /**
    * Adds the token specified to the current linked list of tokens.
    *
    * @param array The character array.
    * @param start The starting offset in the array.
    * @param end The ending offset in the array.
    * @param tokenType The token's type.
    * @param startOffset The offset in the document at which this token
    *        occurs.
    * @param hyperlink Whether this token is a hyperlink.
    */
   public void addToken(char[] array, int start, int end, int tokenType,
                  int startOffset, boolean hyperlink) {
      super.addToken(array, start,end, tokenType, startOffset, hyperlink);
      zzStartRead = zzMarkedPos;
   }

   /**
    * Returns the text to place at the beginning and end of a
    * line to "comment" it in a this programming language.
    *
    * @return The start and end strings to add to a line to "comment"
    *         it out.
    */
   public String[] getLineCommentStartAndEnd() {
      return new String[] { "//", null };
   }

   /**
    * Returns the first token in the linked list of tokens generated
    * from <code>text</code>.  This method must be implemented by
    * subclasses so they can correctly implement syntax highlighting.
    *
    * @param text The text from which to get tokens.
    * @param initialTokenType The token type we should start with.
    * @param startOffset The offset into the document at which
    *        <code>text</code> starts.
    * @return The first <code>Token</code> in a linked list representing
    *         the syntax highlighted text.
    */
   public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

      resetTokenList();
      this.offsetShift = -text.offset + startOffset;

      // Start off in the proper state.
      int state = Token.NULL;

      s = text;
      try {
         yyreset(zzReader);
         yybegin(state);
         return yylex();
      } catch (IOException ioe) {
         ioe.printStackTrace();
         return new TokenImpl();
      }

   }

   /**
    * Refills the input buffer.
    *
    * @return      <code>true</code> if EOF was reached, otherwise
    *              <code>false</code>.
    */
   private boolean zzRefill() {
      return zzCurrentPos>=s.offset+s.count;
   }

   /**
    * Resets the scanner to read from a new input stream.
    * Does not close the old reader.
    *
    * All internal variables are reset, the old input stream
    * <b>cannot</b> be reused (internal buffer is discarded and lost).
    * Lexical state is set to <tt>YY_INITIAL</tt>.
    *
    * @param reader   the new input stream
    */
   public final void yyreset(Reader reader) {
      // 's' has been updated.
      zzBuffer = s.array;
      /*
       * We replaced the line below with the two below it because zzRefill
       * no longer "refills" the buffer (since the way we do it, it's always
       * "full" the first time through, since it points to the segment's
       * array).  So, we assign zzEndRead here.
       */
      //zzStartRead = zzEndRead = s.offset;
      zzStartRead = s.offset;
      zzEndRead = zzStartRead + s.count - 1;
      zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
      zzLexicalState = YYINITIAL;
      zzReader = reader;
      zzAtBOL  = true;
      zzAtEOF  = false;
   }



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public ASMDLTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public ASMDLTokenMaker(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 128) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }
    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }

  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 9: 
          { addToken(Token.RESERVED_WORD);
          }
        case 10: break;
        case 1: 
          { addToken(Token.IDENTIFIER);
          }
        case 11: break;
        case 6: 
          { addToken(Token.LITERAL_NUMBER_FLOAT);
          }
        case 12: break;
        case 7: 
          { addToken(Token.FUNCTION);
          }
        case 13: break;
        case 3: 
          { addToken(Token.WHITESPACE);
          }
        case 14: break;
        case 8: 
          { addToken(Token.DATA_TYPE);
          }
        case 15: break;
        case 2: 
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 16: break;
        case 5: 
          { addNullToken(); return firstToken;
          }
        case 17: break;
        case 4: 
          { addToken(Token.SEPARATOR);
          }
        case 18: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 384: break;
            default:
            return null;
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
