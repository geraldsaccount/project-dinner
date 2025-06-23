import CharacterAvatar from "@/components/shared/character-avatar";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { CharacterDetailDto } from "@/types";
import { ReactNode } from "react";

type Props = {
  character: CharacterDetailDto;
  description?: string;
  children?: ReactNode;
};

const CharacterCard = ({ character, description, children }: Props) => (
  <Card key={character.uuid}>
    <CardHeader className="flex items-center">
      <CharacterAvatar character={character} className="w-20 h-20 mr-4" />
      <div>
        <h4 className="text-lg font-bold">
          {character.name} ({character.age})
        </h4>
        <p className="text-sm text-muted-foreground">{character.role}</p>
      </div>
    </CardHeader>
    <CardContent className="gap-4 flex flex-col justify-between h-full">
      <p className="test-sm text-muted-foreground">
        {description || character.shopDescription}
      </p>
      {children ? (
        <div>
          <Separator className="my-4" />
          {children}
        </div>
      ) : null}
    </CardContent>
  </Card>
);

export default CharacterCard;
