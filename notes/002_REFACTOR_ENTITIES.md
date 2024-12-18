# **Refactoring the entities**

One of the points in the feedback I received was that the entities, such as Account, don't have fields
which might be expected in a real world use case. The assignment did say something on the lines of
'add entities or fields how you see fit'. As the requirements for the task were more focused on the tech-requirements
with vague business-side requirements, I neglected it, which was my mistake of course and this bundle of changes
is to rectify those mistakes, so let's dive in.

## **Refactoring addresses**

In my initial implementation addresses were pretty simple, too simple - contained only a country and a city. However,
what info
should an address entity contain? Browsing the web for any standards, noticed a few that stood out to me:

1) Universal Postal Union (UPU) Standards
2) ISO 19160-1: Addressing Standard

Now, the thing with addresses is, while there are standards, some different rules, formatting, data required differ
based on locale.
As I'd like to keep this demo project simple (at least for now), I'll follow UPU standards, more particularly, UPU S42
addressing
standards - they're more clear and
easier to implement
than the ISO provided standard as well as the UPU standard seems to be more fit for the 'Minibank' potential business
requirements -
the focus shouldn't be on postal services, but on 'banking' functionalities. Anyhow found a neat and clear document
on what fields would be expected for an Address
here - https://www.upu.int/UPU/media/upu/documents/PostCode/S42_International-Addressing-Standards.pdf

I did implement the name and last name fields for the Address entity, as they should be resolved from the owning
Customer entity. However, regarding whether or not a field is required, all of them are IMHO required except:

1) postcode - not all countries have postcodes, e.g. Tuvalu, a small island nation;
2) country - seems to be pretty universal, so marking it as required;
3) postcode - not all countries have postcodes, e.g. Tuvalu, a small island nation;
4) region - this is also applicable mainly to larger countries, and each country has their own way of subdividing into
   regions;
5) town - should be pretty much universal, although one might argue that it could be a house in a 1-house village type
   of ordeal, however, some form of region could be reused here;
6) streetType - at least where I'm from, we don't really use street types, while we do have different types of streets,
   so this seems to be optional;
7) private String streetName - are there addresses without a street? I've yet to encounter this type of situation, so
   leaving it as required for now, making something optional is easier than making it required later on;
8) private String streetNumber - same applies as for street name;
9) floor - if you're living in a flat, that makes sense, however, if you live in a house - it does not, so optional it
   is then.

## **Refactoring accounts**

This one is more foreign to me, while I did work in fintechs, I worked on mostly platform related features, not so much
with such concepts as accounts or transactions. Again, this is a simple demo project, I don't want to go into the
intricacies
on types of accounts, differences between them and differences between accounts based on some locale. When in doubt -
ChatGPT it out!

Small note - added Currency for the account as an enum for now. Is there any data on the currency that might change
frequently? I don't know, however,
it should suffice for this small demo project.

## **Refactoring customers**

Not much to go on about here, similar as accounts, added some useful fields like birthdate or national ID