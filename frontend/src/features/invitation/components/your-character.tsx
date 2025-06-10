import { CharacterDetailDto } from "@/types";
import CharacterProfile from "./character-profile";

type Props = {
  character: CharacterDetailDto;
};

const YourCharacter = ({ character }: Props) => {
  return (
    <>
      <h4 className="text-xl font-bold mt-6 mb-2">
        You'll be stepping in the role of:
      </h4>
      <CharacterProfile character={character} hideUser />
    </>
  );
};

export default YourCharacter;
