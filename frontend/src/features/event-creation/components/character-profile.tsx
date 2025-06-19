import { CharacterDetailDto } from "@/types";

type Props = {
  character: CharacterDetailDto;
};

const CharacterCard = ({ character }: Props) => {
  return (
    <div className="flex items-center gap-4 p-2">
      <img
        src={`data:image/jpeg;base64,${character.avatarData}`}
        alt={character.name}
        className="rounded-full w-16 h-16 object-cover bg-gray-200"
      />
      <div className="flex flex-col">
        <span className="font-semibold text-lg">{character.name}</span>
        <span className="text-muted-foreground text-sm">
          {character.shopDescription}
        </span>
      </div>
    </div>
  );
};

export default CharacterCard;
