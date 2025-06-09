import { CharacterDetailDto, UserDto } from "@/types";

type Props = {
  user?: UserDto;
  character: CharacterDetailDto;
  hideUser?: boolean;
};

const CharacterProfile = ({ user, character, hideUser = false }: Props) => {
  return (
    <div className={`flex items-center gap-4 p-2 `}>
      <img
        src="https://placehold.co/64x64"
        alt={character.name}
        className="rounded-full w-16 h-16 object-cover bg-gray-200"
      />
      <div className="flex flex-col">
        <span
          className={
            "font-semibold text-lg " + (hideUser ? "text-primary" : "")
          }
        >
          {hideUser ? (
            <>{character.name}</>
          ) : (
            <>
              {user?.name ? (
                user.name
              ) : (
                <span className="italic text-muted-foreground">TBD</span>
              )}{" "}
              as {character.name}
            </>
          )}
        </span>
        <span className="text-muted-foreground text-sm">
          {character.shopDescription}
        </span>
      </div>
    </div>
  );
};

export default CharacterProfile;
