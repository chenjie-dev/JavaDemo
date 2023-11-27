package com.chenjie.juc.CAS;

public class CASTest {
    /**
     * CAS：对于内存中的某一个值V，提供一个旧值A和一个新值B。如果提供的旧值V和A相等就把B写入V。这个过程是原子性的。
     * CAS执行结果要么成功要么失败，对于失败的情形下一班采用不断重试。或者放弃。
     * <p>
     *
     * ABA：如果另一个线程修改V值假设原来是A，先修改成B，再修改回成A。当前线程的CAS操作无法分辨当前V值是否发生过变化。
     * <p>
     * 关于ABA问题的一个例子：在你非常渴的情况下你发现一个盛满水的杯子，你一饮而尽。之后再给杯子里重新倒满水。
     * 然后你离开，当杯子的真正主人回来时看到杯子还是盛满水，他当然不知道是否被人喝完重新倒满。
     * <p>
     *
     * 解决这个问题的方案的一个策略是每一次倒水假设有一个自动记录仪记录下，这样主人回来就可以分辨在她离开后是否发生过重新倒满的情况。
     * 这也是解决ABA问题目前采用的策略。
     *
     */
}