import type { CharacterSummary } from "@/types";
import React from "react";

type Props = {
  character: CharacterSummary;
};

const CharacterCard = ({ character }: Props) => {
  return (
    <div className="flex items-center gap-4 p-2">
      <img
        src="https://placehold.co/64x64"
        alt={character.name}
        className="rounded-full w-16 h-16 object-cover bg-gray-200"
      />
      <div className="flex flex-col">
        <span className="font-semibold text-lg">{character.name}</span>
        <span className="text-muted-foreground text-sm">
          {character.characterDescription}
        </span>
      </div>
    </div>
  );
};

export default CharacterCard;
