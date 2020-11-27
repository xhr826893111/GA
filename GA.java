import java.util.List;

public class GA {
    private static double PI = Math.PI;
    private static int G_LENGTH = 32;
    private static int COMBINE_TIMES = 20;
    private static int MUTATION_RATE = 1000;	// 变异概率，1000就是千分之一
    private static int GROUP_SIZE = 100;     // 种群的大小

    public static void main(String[] args) {
        // 生成原始数据
        int[][] group = new int[GROUP_SIZE][G_LENGTH];
        for(int i = 0;i<GROUP_SIZE;i++) {
            group[i] = GA.generateGenenic(G_LENGTH);
        }
        System.out.println("初始数据"+"最大适应度"+ GA.getMaxFit(group));
        int epoch_num = 10000;		// 训练次数
        for(int i= 0;i<epoch_num;i++) {
            // 选择
            group = GA.selection(group);
            // 变异
            GA.mutation(group);
            System.out.println("epoch"+(i+1)+"最大适应度"+ GA.getMaxFit(group));
        }
    }
    // 函数
    // 适应度
    public static double f(double x) {
        double y = 0;
        y = x*Math.sin(10*PI*x) +2;
        return y;

    }

    // 基因编码转换成x
    public static double geneticToX(int[]a) {
        double sum =0;
        double total = 0;
        double x = 0;
        for(int i = 0;i<a.length;i++) {
            sum *= 2;
            total *= 2;
            sum += a[i];
            total += 1;
        }
        // X -1,2
        sum *= 3;
        x = sum/total-1;
        return x;
    }


    // 随机生成编码
    public static int[] generateGenenic(int length) {
        int[] a = new int[length];
        for(int i = 0;i<length;i++) {
            a[i] = (int)(Math.random()*100)%2;
        }
        return a;
    }

    // 获取当前总群中最大适应度
    public static double getMaxFit(int[][] group) {
        double max = Double.MIN_VALUE;
        double[] fits = getFits(group);
        for(int i =0;i<group.length;i++) {
            if(fits[i]>max) {
                max = fits[i];
            }
        }
        return max;
    }

    // 总群结合
    // 选择算子
    public static int[][] selection(int[][] group) {
        // 计算适应度
        double[] fits = getFits(group);
        // 向后累加,方便随机选择
        for(int i = 1;i<group.length;i++) {
            fits[i] += fits[i-1];
        }
        int[][] newGroup = new int[COMBINE_TIMES*2][G_LENGTH];
        int count = 0;
        for(int i = 0;i<COMBINE_TIMES;i++) {
            // 随机选择两个数
            int a,b;
            a = choose(fits);
            b = choose(fits);
            // 结合
            int[][]c = crossover(group[a], group[b]);
            newGroup[count++] = c[0];
            newGroup[count++] = c[1];
        }
        return newGroup;
    }
    //计算适应度
    public static double[] getFits(int[][] group) {
        // 计算适应度
        double[] fits = new double[group.length];
        for(int i = 0;i<group.length;i++) {
            double x = geneticToX(group[i]);
            fits[i] = f(x);

        }
        return fits;
    }
    // 随机选择
    public static int choose(double []fits) {
        // 利用random生成0-1之间的数a，将这个数乘fits[len-1]
        // 然后fits[i] >a > fits[i-1] 则i就是被选中的
        double c = Math.random() * fits[fits.length-1];
        int i = 0;
        for(;i<fits.length;i++) {
            if(c<fits[i])
                break;
        }
        return i;
    }

    // 编码组合
    // 交叉算子
    public static int[][] crossover(int[]a,int[]b) {
        int len = a.length;
        // 随机一个组合点
        int combinePoint = (int)(Math.random()*100)%(len-1) +1;
        int [][]c = new int[2][G_LENGTH];
        for(int i = 0;i<combinePoint;i++) {
            c[0][i] = a[i];
            c[1][i] = b[i];
        }
        for(int i = combinePoint;i<len;i++) {
            c[0][i] = b[i];
            c[1][i] = a[i];
        }
        return c;
    }

    // 变异
    public static void mutation(int[][] group){
        for(int i=0;i<group.length;i++) {
            for(int j = 0 ;j<group[i].length;j++) {
                // 发生变异
                if((int)(Math.random()*100)%MUTATION_RATE == 0)
                    group[i][j] = 1-group[i][j];
            }
        }
    }
}