package contracts.usermgmt.user

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            name("get user")
            request {
                method GET()
                url("/users/1")
            }
            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body([
                        userId: $(anyPositiveInt()),
                        orderId: $(anyPositiveInt())
                ])
            }
        }
]
