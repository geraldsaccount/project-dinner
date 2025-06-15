import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useEditorContext } from "../context/editor-context";
import useAuthenticatedApi from "@/hooks/useAuthenticatedApi";
import { Mystery } from "@/types/creation";

export function useLoadCreationFromRoute() {
  const { storyId } = useParams();
  const { setStory, setCharacters, setStages, setPlayerConfigs, setCrime } =
    useEditorContext();
  const { callApi, data, loading, error } = useAuthenticatedApi<Mystery>();

  useEffect(() => {
    if (storyId) {
      callApi(`/api/editor/${storyId}`);
    }
  }, [storyId, callApi]);

  useEffect(() => {
    if (data) {
      setStory(data.story);
      setCharacters(data.characters);
      setStages(data.stages);
      setPlayerConfigs(data.setups);
      setCrime(data.crime);
    }
  }, [data, setStory, setCharacters, setStages, setPlayerConfigs, setCrime]);

  return { id: storyId, loading, error };
}
