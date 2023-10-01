package tk.estecka.invarpaint.crafting;

public interface IUnsyncRecipe {
	default boolean DontSync(){
		return true;
	}
}
