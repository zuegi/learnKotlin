package learn.dsl.calculation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CacculateDslKtTest {
    @Test
    fun `should calculate addition and then subtract `() {
        val calculation =
            calculate {
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
        assertThat(calculation.calculate()).isEqualTo(0.4)
    }

    @Test
    fun `should calculate with no result so factor2 is set to default which is 0`() {
        val calculation =
            calculate {
                operation {
                    multiplication {
                        factor1 = 0.3
                    }
                }
            }
        assertThat(calculation.calculate()).isEqualTo(0.0)
    }

    @Test
    fun `should calculate addition of 2 numbers and multiply `() {
        val calculation =
            calculate {
                operation {
                    addition {
                        summand1 = 0.2
                        summand2 = 0.3
                    }
                    multiplication {
                        factor1 = 3.0
                        factor2 = 2.0
                    }
                }
            }
        assertThat(calculation.calculate()).isEqualTo(1.0)
    }

    @Test
    fun `should calculate addition of 2 numbers by values of operation `() {
        val calculation =
            calculate {
                operation {
                    addition {
                        summand1 = 0.2
                        summand2 = 0.3
                    }
                    // factor 1 is result of above addition
                    multiplication {
                        factor2 = 2.0
                    }
                }
            }
        assertThat(calculation.calculate()).isEqualTo(1.0)
    }

    @Test
    fun `should calclulate addtion and multiply result by summe`() {
        val calculation =
            calculate {
                operation {
                    val summe =
                        addition {
                            summand1 = 0.2
                            summand2 = 0.3
                        }
                    multiplication {
                        factor1 = summe
                        factor2 = 2.0
                    }
                }
            }
        assertThat(calculation.calculate()).isEqualTo(1.0)
    }

    @Test
    fun `should calculate multiplication`() {
        val calculation =
            calculate {
                operation {
                    multiplication {
                        factor1 = 2.0
                        factor2 = 2.3
                    }
                }
            }
        assertThat(calculation.calculate()).isEqualTo(4.6)
    }

    @Test
    fun `should calculate simple addition`() {
        val calculation =
            calculate {
                operation {
                    addition {
                        summand1 = 0.1
                        summand2 = 0.3
                    }
                }
            }

        assertThat(calculation.calculate()).isEqualTo(0.4)
    }
}
