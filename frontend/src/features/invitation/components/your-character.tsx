import CharacterCard from "@/components/shared/character-card";
import { CharacterDetailDto } from "@/types";

type Props = {
  character: CharacterDetailDto;
};

const YourCharacter = ({ character }: Props) => {
  return (
    <>
      <h4 className="text-xl font-bold mt-6 mb-2">
        You'll be stepping in the role of:
      </h4>
      <CharacterCard character={character} />
    </>
  );
};

export default YourCharacter;
