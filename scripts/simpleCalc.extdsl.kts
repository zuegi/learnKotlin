val calculation = calculate {
    operation {
        addition {
            summand1 = 0.2
            summand2 = 0.3
        }
        subtraction {
            subtrahend = 0.1
        }
    }
}
println("Caclulated: ${calculation.calculate()}")
