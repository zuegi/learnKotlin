package learn.dsl.calculation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalculateDslKtTest {


    @Test
    fun `should calclulate addtion and multiply result by x`() {
        val calculation = calculate {
            operation {
                value1 = 0.2
                value2 = 0.3
                val summe  = addition {
                    summand1 = value1
                    summand2 = value2
                }
                println(summe)
                multiplikation {
                    factor1 = summe
                    factor2 = 2.0
                }
            }
        }
        assertThat(calculation.calculate()).isEqualTo(1.0)
    }

    @Test
    fun `should calculate multiplication`() {
        val calculation = calculate {
            operation {
                value1 = 2.0
                value2 = 2.3
                multiplikation {
                   factor1 = value1
                   factor2 = value2
                }
            }
        }
        assertThat(calculation.calculate()).isEqualTo(4.6)

    }

    @Test
    fun `should calculate simple addition`() {
        val calculation = calculate {

            operation {
                value1 = 0.1
                value2 = 0.3
                addition {
                    summand1 = value1
                    summand2 = value2
                }
            }

        }

        assertThat(calculation.calculate()).isEqualTo(0.4)
    }

}
