class RecognitionAlgorithm {
    double[][] a
    List<Mushroom> X_0_edible_training
    List<Mushroom> X_0_poison_training
    List<Mushroom> trainingSample

    void train(List<Mushroom> X_0_training) {
        def split = X_0_training.split { it.edible }
        X_0_edible_training = split[0] as List<Mushroom>
        X_0_poison_training = split[1] as List<Mushroom>
        trainingSample = X_0_edible_training + X_0_poison_training

        int size = X_0_edible_training[0].attributeVector.size()

        double[][] b = new double[2][size]
        calculationOfAlgorithmParameters(b, 0, X_0_edible_training)
        calculationOfAlgorithmParameters(b, 1, X_0_poison_training)
        println()
        println "b : [\n\t${b[0]}, \n\t${b[1]}\n]"

        double[] b_ = new double[size]
        a = new double[2][size]

        for (int j = 0; j < size; j++) {
            b_[j] = 1 / 2 * (b[0][j] + b[1][j])
        }
        println()
        println "b_ : \n\t${b_}"

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = Math.abs(b[i][j] - b_[j])
            }
        }
        println()
        println "a : [\n\t${a[0]}, \n\t${a[1]}\n]"
        println()
    }

    String control(List<Mushroom> controlSample) {
        double[][] s = new double[controlSample.size()][trainingSample.size()]
        for (int i = 0; i < controlSample.size(); i++) {
            Mushroom x = controlSample[i]
            for (int j = 0; j < trainingSample.size(); j++) {
                Mushroom x_0 = trainingSample[j]
                s[i][j] = this.s(x, x_0, x.edible ? 0 : 1)
            }
        }

        double[][] f = new double[controlSample.size()][2]
        int[][] p_x = new int[controlSample.size()][2]
        int[][] pa_x = new int[controlSample.size()][2]
        int successes = 0;
        int failID = -1
        for (int i = 0; i < controlSample.size(); i++) {
            f[i][0] = this.f(Arrays.copyOfRange(s[i], 0, X_0_edible_training.size()))
            f[i][1] = this.f(Arrays.copyOfRange(s[i], X_0_edible_training.size(), s[i].size()))

            Mushroom currentMushroom = controlSample[i]
            p_x[i][0] = currentMushroom.edible ? 1 : 0
            p_x[i][1] = currentMushroom.edible ? 0 : 1
            pa_x[i][0] = this.p_A(f[i], f[i][0])
            pa_x[i][1] = this.p_A(f[i], f[i][1])

            if (p_x[i][0] != pa_x[i][0]) {
                print "!!! "
                failID = controlSample[i].id
            } else {
                successes++
            }
            String result = pa_x[i][0] == 1 ? "EDIBLE" : "POISONOUS"
            println("i = ${i}, mushroom id=${currentMushroom.id} result is " + result)
        }
        println()

        double rate = successes/controlSample.size()
        String res = "Успешно распознано ${successes} из ${controlSample.size()}. Качесто распознавания = ${rate}"
        if (failID != -1) {
            res += ". Неправильно был распознан, например, гриб с id = ${failID}"
        }
        return res
    }

    private void calculationOfAlgorithmParameters(double[][] b, int i, List<Mushroom> currentClass) {
        for (int j = 0; j < b[i].length; j++) {
            int sum = currentClass
                    .stream()
                    .mapToInt({ x -> x[j] })
                    .sum()

            b[i][j] = 1 / currentClass.size() * sum
        }
    }

    private double s(Mushroom x, Mushroom x_0, int i) {
        double sum1 = Arrays.stream(a[i]).sum()
        double sum2 = 0
        for (int j = 0; j < x.attributeVector.size(); j++) {
            int u = x[j] != x_0[j] ? -1 : 1
            sum2 += u * a[i][j]
        }

        return 1 / sum1 * sum2
    }

    private double f(double[] s_i) {
        Arrays.stream(s_i).max().getAsDouble()
    }

    private int p_A(double[] f, double f_i) {
        double max = Arrays.stream(f).max().getAsDouble()
        Math.abs(max - f_i) <= 0.001 ? 1 : 0
    }
}
