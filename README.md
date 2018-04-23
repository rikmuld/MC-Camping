The Camping Mod 2.0
===================

**Official website**: [rikmuld.com](http://rikmuld.com/camping/information)

Code Structure
--------------

The scala code is structured into four main packages:

 - Features: anything that makes up a feature in the mod can be found here as a package. For example it contains a blocks.tent, camping_inventory and entity.bear package. In those feature packages everything that make up the specific feature is present.
 - Registry: everything from the feature package is registered to minecraft(forge) here.
 - Common: common classes extended / used across features (however most common classes are present in [my core mod](https://github.com/Rikmuld/MC-RMCore)).
 - Utils: common functions used across features (however most utilities are present in [my core mod](https://github.com/Rikmuld/MC-RMCore)).

Anything outside of those packages is more general, such as a library of constant strings and the main class of the mod.