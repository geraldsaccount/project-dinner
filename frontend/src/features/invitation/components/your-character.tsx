import CharacterCard from "@/components/shared/character-card";
import SectionHeader from "@/components/shared/section-header";
import { CharacterDetailDto } from "@/types";

type Props = {
  character: CharacterDetailDto;
};

const YourCharacter = ({ character }: Props) => {
  return (
    <section>
      <SectionHeader title="You'll be stepping in the role of:" />
      <CharacterCard character={character} />
    </section>
  );
};

export default YourCharacter;
