import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { Character } from "@/types/creation";
import { UserPlus, Users } from "lucide-react";
import CharacterForm from "./character-form";
import SectionHeader from "@/components/shared/section-header";
import { useEditorContext } from "../../context/editor-context";
import { useEffect, useMemo } from "react";

const CharactersTab = () => {
  const {
    characters,
    setCharacters,
    activeCharacterId,
    setActiveCharacterId,
    stages: globalStages,
  } = useEditorContext();

  const avatarPreviews = useMemo(() => {
    const previews = new Map<string, string>();
    characters.forEach(char => {
      if (char.avatarImage instanceof File) {
        previews.set(char.id, URL.createObjectURL(char.avatarImage));
      }
    });
    return previews;
  }, [characters]);

  useEffect(() => {
    return () => {
      avatarPreviews.forEach(url => URL.revokeObjectURL(url));
    };
  }, [avatarPreviews]);

  const addCharacter = () => {
    const newChar: Character = {
      id: `char_${Date.now()}`,
      name: `New Character ${characters.length + 1}`,
      role: `Archetype`,
      isPrimary: false,
      age: 0,
      gender: "Other",
      shopDescription: "",
      privateDescription: "",
      avatarImage: null,
      relationships: {},
      stageInfo: globalStages.map((s) => ({
        stageId: s.id,
        order: s.order,
        objectivePrompt: "",
        events: [],
      })),
    };
    setCharacters((prev) => [...prev, newChar]);
    setActiveCharacterId(newChar.id);
  };

  const updateCharacter = (id: string, updatedCharacter: Character) =>
    setCharacters((prev) =>
      prev.map((c) => (c.id === id ? updatedCharacter : c))
    );

  const deleteCharacter = (id: string) => {
    if (window.confirm("Are you sure?")) {
      setCharacters((prev) => prev.filter((c) => c.id !== id));
      if (activeCharacterId === id) setActiveCharacterId(null);
    }
  };

  const activeCharacter = characters.find((c) => c.id === activeCharacterId);

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <SectionHeader title="Characters" />
        <Button onClick={addCharacter}>
          <UserPlus /> Add Character
        </Button>
      </div>
      <Card className="w-full">
        <CardHeader>
          <CardTitle>Character Roster</CardTitle>
        </CardHeader>
        <CardContent className="h-full overflow-x-auto character-list">
          <ul className="flex flex-row space-x-2 min-w-full">
            {characters.length === 0 ? (
              <li className="p-3 text-center w-full">No characters yet.</li>
            ) : (
              characters.map((char) => (
                <li key={char.id} className="flex-shrink-0">
                  <a
                    href="#"
                    onClick={(e) => {
                      e.preventDefault();
                      setActiveCharacterId(char.id);
                    }}
                    className={cn(
                      "flex items-center gap-3 p-3 rounded-lg w-54 border transition-colors",
                      activeCharacterId === char.id
                        ? "bg-muted border-muted-foreground"
                        : "hover:bg-slate-100"
                    )}
                  >
                    <img
                      src={
                        avatarPreviews.get(char.id) || // Use memoized preview if available
                        (typeof char.avatarImage === 'string' ? char.avatarImage : null) || // Use string if it's a URL
                        "https://placehold.co/100x100/e2e8f0/64748b?text=Avatar"
                      }
                      className="w-10 h-10 rounded-full object-cover"
                    />
                    <span className="font-medium flex-1 text-center">
                      {char.name}
                    </span>
                  </a>
                </li>
              ))
            )}
          </ul>
        </CardContent>
      </Card>
      {activeCharacter ? (
        <CharacterForm
          character={activeCharacter}
          updateCharacter={updateCharacter}
          deleteCharacter={deleteCharacter}
          allCharacters={characters}
          globalStages={globalStages}
        />
      ) : (
        <div className="h-full flex flex-col items-center justify-center">
          <Users />
          <p>Select a character or add a new one.</p>
        </div>
      )}
    </div>
  );
};

export default CharactersTab;
