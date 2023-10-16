package frontend.parser.node;

public enum ExpType
{
    INT,        // 整数类型
    ARRAY1D,    // 一维数组类型
    ARRAY2D,    // 二维数组类型
    VOID,        // 无返回值类型（例如函数调用）
    ERROR        // 错误类型

}