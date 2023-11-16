import ir.utils.IOUtils;

public class Config
{
    /**
     * stages of compilation
     */
    public static boolean lexer = false;
    public static boolean parser = false;
    public static boolean error = false;
    public static boolean ir = true;

    /**
     * optimization level
     */
    public static boolean chToStr = true;
    public static boolean addToMul = true;

    public static void init() {
        IOUtils.delete("output.txt");
        IOUtils.delete("error.txt");
        IOUtils.delete("llvm_ir.txt");
        IOUtils.delete("mips.txt");
    }
}
