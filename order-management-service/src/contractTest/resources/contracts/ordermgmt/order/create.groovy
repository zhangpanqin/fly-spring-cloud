package contracts.ordermgmt.order

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            name("create order")
            request {
                method POST()
                url("/orders")
                headers {
                    contentType applicationJson()
                }
                body([
                        orderDescription: $(c(anyNonBlankString()), p("any")),
                        price: $(c(anyDouble()), p(1.0)),
                ])
            }
            response {
                status CREATED()
                headers {
                    contentType applicationJson()
                }
                body(
                        id: $(anyPositiveInt())
                )
            }
        }
]
