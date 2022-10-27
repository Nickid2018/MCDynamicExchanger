package io.github.nickid2018.mcde.asmdl.highlight;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;
%%

%public
%class ASMDLTokenMaker
%extends AbstractJFlexCTokenMaker
%unicode
%type org.fife.ui.rsyntaxtextarea.Token

/**
 * A simple TokenMaker example.
 */
%{
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
      return new String[] { "#", null };
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

%}

Letter                     = [A-Za-z]
Digit                     = ([0-9])
IdentifierStart               = ({Letter}|"_")
IdentifierPart                  = ({IdentifierStart}|{Digit}|"/"|";")
WhiteSpace            = ([ \t\f]+)

IntegerLiteral         = ({Digit}+)
FloatingPointLiteral   = ({Digit}+ "." {Digit}+)

Separator               = ([\(\)\{\}\[\]])
Separator2            = ([,])

Identifier            = ({IdentifierStart}{IdentifierPart}*)

LineCommentBegin         = "#"

%%

<YYINITIAL> {

   /* Keywords */
   "class" | "method" | "field" | "label" | "true" | "false" | "value" | "signature" | "extends" |
   "public" | "protected" | "private" | "static" | "final" | "synthetic" | "abstract" | "implements" |
   "interface" | "super" | "enum" | "bridge" | "synchronized" | "volatile" | "transient" | "throws" |
   "native" | "varargs" | "strict" | "outerclass" | "annotation" | "nesthost" | "nestmember" | "innerclass" { addToken(Token.RESERVED_WORD); }

   /* Data types */
   "byte" | "char" | "double" | "long" | "float" | "int" | "short" | "boolean" | "handle" | "type" | "string" { addToken(Token.DATA_TYPE); }

   /* Functions */
   "lookupswitch" | "isub" | "if_icmpeq" | "aaload" | "laload" | "iconst_m1" | "if_icmpne" |
   "invokevirtual" | "freturn" | "lstore" | "multianewarray" | "fsub" | "monitorenter" |
   "sastore" | "astore" | "bastore" | "if_icmpge" | "iload" | "pop2" | "putfield" | "bootstrap" |
   "anewarray" | "ldc_dynamic" | "dup2" | "athrow" | "caload" | "fconst_0" | "if_icmpgt" | "fconst_1" |
   "fdiv" | "frem" | "iaload" | "jsr" | "sipush" | "getfield" | "aconst_null" | "aload" | "invokespecial" |
   "pop" | "goto" | "fadd" | "iflt" | "permitted" | "monitorexit" | "f2d" | "fneg" | "castore" |
   "areturn" | "f2i" | "fconst_2" | "baload" | "f2l" | "ireturn" | "lsub" | "ifle" | "annotation_param_count" |
   "lcmp" | "dsub" | "ifnonnull" | "lload" | "if_acmpeq" | "arraylength" | "if_acmpne" |
   "return" | "fcmpl" | "ddiv" | "dneg" | "constant" | "dastore" | "ishr" | "ladd" | "imul" | "ifge" |
   "newarray" | "fcmpg" | "aastore" | "invokestatic" | "checkcast" | "dadd" | "lneg" | "ldiv" | "i2b" |
   "drem" | "i2d" | "i2c" | "fload" | "lrem" | "i2f" | "ishl" | "irem" | "ret" | "new" | "fstore" |
   "i2l" | "swap" | "ifne" | "iadd" | "dreturn" | "i2s" | "invokedynamic" | "fmul" |
   "istore" | "dup_x1" | "dup_x2" | "ineg" | "faload" | "iastore" | "ifeq" | "idiv" | "lushr" | "dup2_x1" |
   "dup2_x2" | "lastore" | "iconst_5" | "getstatic" | "line" | "putstatic" | "fastore" | "daload" | "nop" |
   "dmul" | "dstore" | "record_component" | "if_icmple" | "bipush" | "parameter" | "iconst_0" | "d2f" |
   "ifnull" | "d2i" | "iconst_3" | "lconst_1" | "dconst_0" | "iconst_4" | "lconst_0" | "dconst_1" |
   "iconst_1" | "iconst_2" | "l2d" | "d2l" | "try_catch" | "l2f" | "l2i" | "lshr" | "iinc" | "dcmpl" |
   "ldc" | "lmul" | "dcmpg" | "if_icmplt" | "iushr" | "instanceof" | "saload" | "lreturn" | "tableswitch" |
    "dload" | "lshl" | "ifgt" | "invokeinterface" | "dup" | "local" { addToken(Token.FUNCTION); }

   {Identifier}            { addToken(Token.IDENTIFIER); }

   {WhiteSpace}            { addToken(Token.WHITESPACE); }

   /* Separators. */
   {Separator}               { addToken(Token.SEPARATOR); }
   {Separator2}            { addToken(Token.IDENTIFIER); }

   /* Numbers */
   {IntegerLiteral}         { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
   {FloatingPointLiteral}   { addToken(Token.LITERAL_NUMBER_FLOAT); }

   {LineCommentBegin}.*      { addToken(Token.COMMENT_EOL); addNullToken(); return firstToken; }

   /* Ended with a line not in a string or comment. */
   \n |
   <<EOF>>                 { addNullToken(); return firstToken; }

   /* Catch any other (unhandled) characters. */
   .                      { addToken(Token.IDENTIFIER); }
}