# **Refactoring the service layer**

A short introduction I'd imagine is required. I've written this project for an assignment and, upon completion, received some feedback. The purpose of this doc (and any others that might pop up
in the notes directory) is to reflect on some of the things that were done, how could have they be done better, perhaps some rants, what's my though process etc. I'm mainly making this doc for myself,
however, perhaps some of the explanations/reasoning provided might prove helpful to some. Anyhow, let's jump into it.

## **Usage of DTOs between services**

Okay, I got some flak because of this one and in my opinion it was well deserved. See, one of the main requirements for the application
was to create an account when creating a customer and link that account to the customer. I thought 'well, the services should be pretty independent' (cue irony) and if we use DTOs for inputs and outputs
in services, that should apply to **all** services, **even when it's service-to-service calls** as to keep consistency.

Now, if we exclusively use DTOs as input output of our services, there might be some issues:
1. Testing - it may become harder, as we’d also have to account for conversion to DTOs in the service layer
2. If we for some reason need service-to-service interactions, it would require Entity-DTO-Entity conversions, which also might include extra queries in some cases (checking whether or not a parent entity exists when creating a child entity)
   
My first approach was strictly using DTOs, which led to problems mentioned above and thus well deserved comments questioning such an approach. Again, original reasoning - each service should be somewhat independent, e.g. you could call the 'create an account' method not only when creating a customer, but also just when creating an account itself, but there are better ways of doing this. 

Now, if we use Entities as return types - that’s also not that great IMO. Although it would allow avoiding additional queries. The idea that an Entity is present in all of the application layers (presentation, service, data) feels so wrong. Ideally one structure should only be used between two layers, which tends to happens when following DDD (domain driven design) practices, but not going down that path just yet. Also, what if we had an endpoint for creating an account by itself, without creating a customer and using an existing one? I wouldn’t quite encumber the controller to handle first fetching a customer then providing it to the account service.

So, considering the options we have, adding a layer on top of services would make sense. We would use an orchestrator - calling multiple services if required. The issue I’m seeing with this - it will add bloat. For example, we will probably have a ‘find customer by id’ use case and in such a case, the ‘orchestrator’ would act only as a proxy to the service without actually having any other steps (other than maybe calling DTO-entity and entity-DTO conversion components). Again, this could be considered a bad practice somewhat, code is a liability and all that, however, IMHO this will have more upsides:
1. No need to re-query entities on operations related to multiple entities.
2. Services only depend on the layer below and don’t depend on each other.
   
Regarding the added ‘bloat’, my strong opinion is that consistency is more important in this case, even though this demo project is quite small. It’s more convenient to work and develop the application knowing what to expect from what component/layer, less mishmash/maintenance overhead.

## **Updates of customer addresses**

Alright, this one's more of a functional requirement adjustment. One of the requirements for the application
was to be able to **update** the customer along with the customer's addresses. What I implemented:

1) If you're passing an address in the list of addresses in the DTO and it does not have an ID - that means that's a new address so create it and add it for the customer. Example, if a customer was created without any addresses and we want to add one.
2) If you're passing and address with an ID present that means update the address. Let's say an address change, we don't necessarily want to delete it, maybe just adjust it.
3) If you're passing an empty address list - remove all addresses.
4) If you're passing an address with an ID that's not already present for the customer - throw an exception, the address might not belong to that customer and assigning it could remove for the other customer.

I'm really curious whether the reader would approve such an approach, although not having much context does not help. I got feedback, that if only updates are asked, then only updates are required so fair enough, my fail here. Thus I'm changing it up a bit,
removing the logic of the 1st and 3rd points, keeping the 4th just for good measure.

## **Updates of test**

This one is pretty basic - the tests were pretty bad, I rushed them a bit
and practically tested mocking rather than the service under test. Anyhow, by introducing
the orchestration classes, tests should be a bit more concise and cleaner now without over-mocking. 